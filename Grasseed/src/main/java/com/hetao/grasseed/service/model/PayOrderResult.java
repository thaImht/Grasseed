package com.hetao.grasseed.service.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayOrderResult {

    /**
     * 支付单号
     */
    private String paymentId;

    /**
     * 支付渠道支付单号
     */
    private String outPaymentId;

    /**
     * 支付状态
     */
    private String status;

    /**
     * 支付渠道支付单状态
     */
    private String outStatus;

    /**
     * 订单金额
     */
    private Integer payAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 支付订单状态的描述和下一步操作的指引
     */
    private String statusDescription;

    /**
     * 失败原因
     */
    private String failReason;

}
