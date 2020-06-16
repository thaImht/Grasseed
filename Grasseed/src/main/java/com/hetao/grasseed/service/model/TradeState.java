package com.hetao.grasseed.service.model;

import com.hetao.grasseed.model.enumeration.OrderStatusEnum;

import lombok.Getter;

@Getter
public enum TradeState {

    SUCCESS("支付成功", OrderStatusEnum.PAID),
    REFUND("转入退款", OrderStatusEnum.REFUNDING),
    NOTPAY("未支付", OrderStatusEnum.PAYING),
    CLOSED("已关闭", OrderStatusEnum.CLOSED),
//    REVOKED("已撤销(刷卡支付)", PaymentOrderStatus.FAIL),
    USERPAYING("用户支付中", OrderStatusEnum.PAYING),
    PAYERROR("支付失败", OrderStatusEnum.FAIL);

    private String desc;
    private OrderStatusEnum orderStatus;

    TradeState(String desc, OrderStatusEnum orderStatus) {
        this.desc = desc;
        this.orderStatus = orderStatus;
    }

}
