package com.hetao.grasseed.model.response;

import lombok.Data;

@Data
public class OrderNotifyResponse {

    /**
     * 商户系统内部订单号 String(32)
     */
    private String outTradeNo;

    /**
     * 微信支付订单号 String(32), 支付成功时不为空
     */
    private String transactionId;

    /**
     * 订单总金额，单位为分, 支付成功时不为空
     * <p>注: 没有代金款，total_fee即为收款金额<p/>
     */
    private Integer totalFee;

    /**
     * 订单支付时间，格式为yyyyMMddHHmmss, 支付成功时不为空
     */
    private String timeEnd;

}
