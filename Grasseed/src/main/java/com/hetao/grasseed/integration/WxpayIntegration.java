package com.hetao.grasseed.integration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hetao.grasseed.common.util.CryptUtil;
import com.hetao.grasseed.common.util.NumberUtil;
import com.hetao.grasseed.integration.exception.IntegrationException;
import com.hetao.grasseed.model.request.UnifiedOrderJsapiRequest;
import com.hetao.grasseed.model.response.OrderNotifyResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WxpayIntegration {

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.pay.mch_id}")
    private String mchId;

    @Value("${wx.pay.key}")
    private String key;

    @Value("${wx.pay.baseUrl}")
    private String baseUrl;

    private WxPayRequest wxPayRequest;

    private String config = "ae57fdfd7f36b5825d51ccf710aaa107";
    
    @PostConstruct
    public void init() throws Exception {

//        mchId = CryptUtil.pkcs7Decode(
//                CryptUtil.base64Decode(mchId),
//                config.getBytes()
//        );
//
//        key = CryptUtil.pkcs7Decode(
//                CryptUtil.base64Decode(key),
//                config.getBytes()
//        );

        InputStream in = WxpayIntegration.class.getResourceAsStream("/config/apiclient_cert.p12");
        try {
            wxPayRequest = new WxPayRequest(in, mchId.toCharArray());
        } finally {
            IOUtils.closeQuietly(in);
        }

        //沙箱环境key会变，每次启动重新获取一下
        if( baseUrl.contains("sandboxnew") ) {
            key = getSignKey();
        }
    }

    /**
     *
     * @param request
     * @return
     * @throws IntegrationException
     */
    public Map<String, String> jsapiOrder(UnifiedOrderJsapiRequest request) throws IntegrationException {
        Map<String, String> req = new HashMap<>();
        req.put("appid", appid);
        req.put("mch_id", mchId);
        req.put("nonce_str", WxPayUtil.generateUUID());
        req.put("body", request.getBody());
        req.put("out_trade_no", request.getOutTradeNo());
        req.put("fee_type", "CNY");
        req.put("total_fee", request.getTotalFee().toString());
        req.put("spbill_create_ip", request.getSpbillCreateIp());
        req.put("time_start", request.getTimeStart());
        String timeExpire = request.getTimeExpire();
        if( !StringUtils.isEmpty(timeExpire) ) {
            req.put("time_expire", request.getTimeExpire());
        }
        req.put("notify_url", request.getNotifyUrl());
        req.put("trade_type", "JSAPI");
        req.put("openid", request.getOpenid());
        req.put("sign", WxPayUtil.generateSignature(req, key, WxSignType.MD5));
        Map<String, String> response = unifiedOrder(req);

        Map<String, String> payMap = new HashMap<>();
        payMap.put("appId", appid);
        payMap.put("timeStamp", WxPayUtil.getCurrentTimestamp()+"");
        payMap.put("nonceStr", WxPayUtil.generateNonceStr());
        payMap.put("package", "prepay_id=" + response.get("prepay_id"));
        payMap.put("signType", WxSignType.MD5.getName());
        payMap.put("paySign", WxPayUtil.generateSignature(payMap, key, WxSignType.MD5));
        return payMap;
    }


    private Map<String, String> unifiedOrder(Map<String, String> req) throws IntegrationException {
        String reqBody = WxPayUtil.mapToXml(req);
        log.info("H5WxpayIntegration unifiedorder request: {}", reqBody);
        String respXml;
        try {
            respXml = wxPayRequest.requestWithoutCert(baseUrl + "/pay/unifiedorder", reqBody);
            log.info("H5WxpayIntegration unifiedorder response: {}", respXml);
        } catch (IOException e) {
            log.error("H5WxpayIntegration unifiedorder error", e);
            throw new IntegrationException(e);
        }
        Map<String, String> response = processResponseXml(respXml);
        processError(response);
        return response;
    }

    public OrderNotifyResponse orderNotify(String notifyXml) throws IntegrationException {
        Map<String, String> resp = processResponseXml(notifyXml);
        processError(resp);

        OrderNotifyResponse response = new OrderNotifyResponse();
        response.setOutTradeNo(resp.get("out_trade_no"));
        response.setTransactionId(resp.get("transaction_id"));
        response.setTotalFee(NumberUtil.toInteger(resp.get("total_fee")));
        response.setTimeEnd(resp.get("time_end"));
        return response;
    }
    
    //支付单号不存在也会返回成功
    public void closeOrder(String outTradeNo) throws IntegrationException {
        Map<String, String> req = new HashMap<>();
        req.put("appid", appid);
        req.put("mch_id", mchId);
        req.put("out_trade_no", outTradeNo);
        req.put("nonce_str", WxPayUtil.generateUUID());
        req.put("sign", WxPayUtil.generateSignature(req, key, WxSignType.MD5));
        String reqBody = WxPayUtil.mapToXml(req);
        log.info("H5WxpayIntegration closeOrder request: {}", reqBody);
        String respXml;
        try {
            respXml = wxPayRequest.requestWithoutCert(baseUrl + "/pay/closeorder", reqBody);
            log.info("H5WxpayIntegration closeOrder response: {}", respXml);
        } catch (IOException e) {
            log.error("H5WxpayIntegration closeOrder error", e);
            throw new IntegrationException(e);
        }

        Map<String, String> resp = processResponseXml(respXml);
        processError(resp);
    }

    private String getSignKey() throws IntegrationException {
        Map<String, String> req = new HashMap<>();
        req.put("mch_id", mchId);
        req.put("nonce_str", WxPayUtil.generateUUID());
        req.put("sign",  WxPayUtil.generateSignature(req, key, WxSignType.MD5));
        String reqBody = WxPayUtil.mapToXml(req);
        log.info("H5WxpayIntegration getSign request: {}", reqBody);
        String respXml;
        try {
            respXml = wxPayRequest.requestWithoutCert("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", reqBody);
            log.info("H5WxpayIntegration getSign response: {}", respXml);
        } catch (IOException e) {
            log.error("H5WxpayIntegration getSign error", e);
            throw new IntegrationException(e);
        }

        Map<String, String> respData = WxPayUtil.xmlToMap(respXml);
        String return_code = respData.get(WxPayUtil.RETURN_CODE);
        if (return_code.equals(WxPayUtil.FAIL)) {
            String returnMsg = respData.get("return_msg");
            log.error("return_code is fail");
            throw new IntegrationException(returnMsg);
        } else if (return_code.equals(WxPayUtil.SUCCESS)) {
            return respData.get("sandbox_signkey");
        } else {
            throw new RuntimeException(String.format("return_code value %s is invalid in XML: %s", return_code, respXml));
        }
    }

    private void processError( Map<String, String> response) throws IntegrationException {
        String resultCode = response.get(WxPayUtil.RESULT_CODE);
        //有些响应报文没有result_code
        if ( resultCode == null || WxPayUtil.SUCCESS.equals(resultCode) ) {
            return;
        } else if ( WxPayUtil.FAIL.equals(resultCode) ) {
            log.error("result_code is fail");
            String errCodeDes = response.get("err_code_des");
            throw new IntegrationException(errCodeDes);
        } else {
            throw new RuntimeException("result_code value is invalid: " + resultCode);
        }
    }

    /**
     * 处理 HTTPS API返回数据，转换成Map对象。return_code为SUCCESS时，验证签名。
     * @param xmlStr API返回的XML格式数据
     * @return Map类型数据
     * @throws Exception
     */
    private Map<String, String> processResponseXml(String xmlStr) throws IntegrationException {
        return processResponseXml(xmlStr, true, true);
    }

    private Map<String, String> processResponseXml(String xmlStr, boolean throwExceptionIfFail) throws IntegrationException {
        return processResponseXml(xmlStr, throwExceptionIfFail, true);
    }

    private Map<String, String> processResponseXml(String xmlStr, boolean throwExceptionIfFail, boolean validateSign) throws IntegrationException {
        String return_code;
        Map<String, String> respData = WxPayUtil.xmlToMap(xmlStr);
        if (respData.containsKey(WxPayUtil.RETURN_CODE)) {
            return_code = respData.get(WxPayUtil.RETURN_CODE);
        } else {
            throw new RuntimeException(String.format("No `return_code` in XML: %s", xmlStr));
        }

        if (return_code.equals(WxPayUtil.FAIL)) {
            log.error("return_code is fail");
            if( throwExceptionIfFail ) {
                String returnMsg = respData.get("return_msg");
                throw new IntegrationException(returnMsg);
            } else {
                return respData;
            }
        } else if (return_code.equals(WxPayUtil.SUCCESS)) {
            if ( !validateSign || this.isResponseSignatureValid(respData) ) {
                return respData;
            } else {
                throw new RuntimeException(String.format("Invalid sign value in XML: %s", xmlStr));
            }
        } else {
            throw new RuntimeException(String.format("return_code value %s is invalid in XML: %s", return_code, xmlStr));
        }
    }

    /**
     * 判断xml数据的sign是否有效，必须包含sign字段，否则返回false。
     *
     * @param reqData 向wxpay post的请求数据
     * @return 签名是否有效
     * @throws Exception
     */
    private boolean isResponseSignatureValid(Map<String, String> reqData)  {
        // 返回数据的签名方式和请求中给定的签名方式是一致的
        return WxPayUtil.isSignatureValid(reqData, key);
    }

    public static void main(String[] args) throws Exception {
        WxpayIntegration wxpay = new WxpayIntegration();
        wxpay.init();

//        {
//            UnifiedOrderH5Request request = new UnifiedOrderH5Request();
//            request.setBody("test");
//            request.setOutTradeNo("T6402803932267024383");
//            request.setSpbillCreateIp("127.0.0.1");
//            request.setTimeStart("20180517172000");
//            request.setTotalFee(502);
//            request.setWapName("wwww");
//            request.setWapUrl("");
//            request.setNotifyUrl("localhost");
//            String mweb_url = wxpay.unifiedOrder(request);
//            System.out.println(mweb_url);
//        }

//        {
//            System.out.println(
//                    wxpay.orderQuery("T6402803932267024383")
//            );
//        }

//        {
//            RefundRequest request = new RefundRequest();
//            request.setOutTradeNo("T6402803932267024383");
//            request.setNotifyUrl("localhost");
//            request.setOutRefundNo("T64028039322670243831");
//            request.setRefundDesc("test");
//            request.setRefundFee(502);
//            request.setTotalFee(502);
//            System.out.println(
//                    wxpay.refund(request)
//            );
//        }

//        {
//            System.out.println(
//                    wxpay.refundQuery("sandbox_out_refund_no_0")
//            );
//        }

//        {
//            DownloadBillRequest request = new DownloadBillRequest();
//            request.setBillDate("20180520");
//            request.setBillType("ALL");
//            System.out.println(
//                    wxpay.downloadbill(request)
//            );
//        }
    }

}
