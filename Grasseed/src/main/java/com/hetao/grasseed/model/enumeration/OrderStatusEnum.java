package com.hetao.grasseed.model.enumeration;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
	CREATE("0","创建"), 
	PAYING("1","支付中"),
    PAID("2","支付完成"),
    REFUNDING("3","退款中"),
    REFUNDED("4","退款完成"),
	CLOSED("5","关闭"),
	FAIL("6","失败");

    private String status;
    private String statusName;
    private OrderStatusEnum(String status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }
}
