package com.hetao.grasseed.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.hetao.grasseed.common.util.DateUtil;
import com.hetao.grasseed.exception.ApplicationException;
import com.hetao.grasseed.integration.WxpayIntegration;
import com.hetao.grasseed.integration.exception.IntegrationException;
import com.hetao.grasseed.model.response.OrderNotifyResponse;
import com.hetao.grasseed.service.ChannelPayService;
import com.hetao.grasseed.service.model.PayOrderResult;
import com.hetao.grasseed.service.model.TradeState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class WxPayBaseChannelService implements ChannelPayService {

	@Autowired
    protected WxpayIntegration wxpayIntegration;
	
	@Override
    public PayOrderResult orderNotify(String notification) throws ApplicationException {
        OrderNotifyResponse response;
        try {
            response = wxpayIntegration.orderNotify(notification);
        } catch (IntegrationException e) {
            log.error("wxpayIntegration.orderNotify error: {}", e.getMessage());
            throw new RuntimeException("微信支付回调通知异常");
        }

        PayOrderResult result = new PayOrderResult();
        result.setPaymentId(response.getOutTradeNo());
        result.setPayTime(DateUtil.parseSimpleTimestamp(response.getTimeEnd()));
        result.setStatus(TradeState.SUCCESS.getOrderStatus().getStatus());
        result.setOutStatus(TradeState.SUCCESS.toString());
        Integer totalFee = response.getTotalFee();
        if( totalFee != null ) {
            result.setPayAmount(totalFee);
        }
        result.setOutPaymentId(response.getTransactionId());
        result.setStatusDescription(TradeState.SUCCESS.getDesc());
        return result;
    }
}
