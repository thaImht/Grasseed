package com.hetao.grasseed.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.hetao.grasseed.common.util.DateUtil;
import com.hetao.grasseed.exception.ApplicationException;
import com.hetao.grasseed.integration.WxpayIntegration;
import com.hetao.grasseed.model.request.PayOrderRequest;
import com.hetao.grasseed.model.request.UnifiedOrderJsapiRequest;
import com.hetao.grasseed.model.response.PayOrderResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class WxPayJsapiChannelServiceImpl extends WxPayBaseChannelService {

	@Value("${wx.pay.payNotifyUrl}")
    protected String payNotifyUrl;
	
	@Autowired
    protected WxpayIntegration wxpayIntegration;
	
    @Override
    public PayOrderResponse order(PayOrderRequest request) throws ApplicationException {
        String extraParams = request.getExtraParams();
        JSONObject params = JSON.parseObject(extraParams);
        String openid = params.getString("openid");

        UnifiedOrderJsapiRequest req = new UnifiedOrderJsapiRequest();
        req.setBody("微信支付");
        req.setOutTradeNo(request.getPaymentId());
        req.setTotalFee(request.getPayAmount());
        req.setSpbillCreateIp(request.getClientIp());
        req.setTimeStart(DateUtil.formatSimpleTimestamp(new Date()));
        req.setTimeExpire(DateUtil.formatSimpleTimestamp(request.getExpireTime()));
        req.setNotifyUrl(payNotifyUrl);
        req.setOpenid(openid);
        Map<String, String> payMap;
        try {
            payMap = wxpayIntegration.jsapiOrder(req);
        }  catch (Throwable e) {
            throw Throwables.propagate(e);
        }

        PayOrderResponse payOrderResponse = new PayOrderResponse();
        payOrderResponse.setOrderId(request.getPaymentId());
        payOrderResponse.setValue(JSON.toJSONString(payMap));
        return payOrderResponse;
    }

	@Override
	public void closeOrder(String paymentId) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

}
