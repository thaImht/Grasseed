package com.hetao.grasseed.service;

import com.hetao.grasseed.exception.ApplicationException;
import com.hetao.grasseed.model.request.PayOrderRequest;
import com.hetao.grasseed.model.response.PayOrderResponse;
import com.hetao.grasseed.service.model.PayOrderResult;

public interface ChannelPayService {

    /**
     * 支付单下单接口
     * @param request
     * @return
     * @throws ApplicationException 通过getRawException可获得原始的异常，如果是IOException可认为是请求未获得结果
     */
    PayOrderResponse order(PayOrderRequest request) throws ApplicationException;

    /**
     * 支付回调接口，如果支付状态是成功，需要检查订单金额
     * @param notification 通知的json或者xml内容，如果是form表单，则是request.getParameterMap()的json表示
     * @return
     * @throws ApplicationException
     */
    PayOrderResult orderNotify(String notification) throws ApplicationException;
    
    /**
     * 关闭支付单接口
     * @param paymentId
     * @throws ApplicationException 通过getRawException可获得原始的异常，如果是IOException可认为是请求未获得结果
     */
    void closeOrder(String paymentId) throws ApplicationException;

    

}
