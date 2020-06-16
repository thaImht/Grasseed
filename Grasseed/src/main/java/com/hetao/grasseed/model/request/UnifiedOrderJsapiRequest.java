package com.hetao.grasseed.model.request;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class UnifiedOrderJsapiRequest {

    /**
     * 商品简单描述 String(128)
     */
    private String body;

    /**
     * 商户系统内部的订单号 String(32)
     */
    private String outTradeNo;

    /**
     * 订单总金额，单位为分
     */
    private Integer totalFee;

    /**
     * 用户端IP String(16)
     */
    private String spbillCreateIp;

    /**
     * 订单生成时间，格式为yyyyMMddHHmmss
     */
    private String timeStart;

    /**
     * 订单失效时间，格式为yyyyMMddHHmmss
     */
    @Nullable
    private String timeExpire;

    /**
     * 接收微信支付异步通知回调地址 String(256)
     */
    private String notifyUrl;

    /**
     * 微信用户在商户对应appid下的唯一标识
     */
    private String openid;

}
