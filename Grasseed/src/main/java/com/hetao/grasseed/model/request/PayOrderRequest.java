package com.hetao.grasseed.model.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class PayOrderRequest {

    /**
     * 商品描述
     */
    private String orderDescription;

    /**
     * 支付单号
     */
    private String paymentId;

    /**
     * 支付订单金额, 单位元
     */
    private Integer payAmount;

    /**
     * 用户端IP
     */
    private String clientIp;

    /**
     * 支付订单生成时间
     */
    private Date createTime;

    /**
     * 支付订单失效时间
     */
    private Date expireTime;

    /**
     * 接收异步通知回调地址
     */
    private String notifyUrl;

    /**
     * 支付完成后的跳转地址
     */
    //private String returnUrl;

    /**
     * 和支付方式相关的一些额外参数
     */
    private String extraParams;

}
