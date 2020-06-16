package com.hetao.grasseed.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hetao.grasseed.dao.MallOrderRepository;
import com.hetao.grasseed.dao.OrderPurchaseRepository;
import com.hetao.grasseed.dao.ProductRepository;
import com.hetao.grasseed.model.entity.MallOrder;
import com.hetao.grasseed.model.entity.OrderPurchase;
import com.hetao.grasseed.model.entity.Product;
import com.hetao.grasseed.model.enumeration.OrderStatusEnum;
import com.hetao.grasseed.model.request.OneProductRequest;
import com.hetao.grasseed.model.request.PayOrderRequest;
import com.hetao.grasseed.model.response.GrasseedResponse;
import com.hetao.grasseed.model.response.GrasseedResponseBuilder;
import com.hetao.grasseed.model.response.PayOrderResponse;
import com.hetao.grasseed.service.ChannelPayService;
import com.hetao.grasseed.service.MallService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MallServiceImpl implements MallService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private MallOrderRepository mallOrderRepository;
	
	@Autowired
	private OrderPurchaseRepository orderPurchaseRepository;
	
	@Resource(name = "wxPayJsapiChannelServiceImpl")
	private ChannelPayService wxPayJsapiService;
	
	@Override
	public GrasseedResponse productList() {
		List<Product> products = productRepository.findByIsOnlineTrue();
		return GrasseedResponseBuilder.buildSuccssResponse(products);
	}

	@Override
	public GrasseedResponse productDetail(OneProductRequest req) {
		Product product = productRepository.findByProductCode(req.getProductCode());
		return GrasseedResponseBuilder.buildSuccssResponse(product);
	}

	@Override
	public MallOrder addOrder(String openId, String productCode, String price) {
		MallOrder order = new MallOrder();
		order.setFromChannel("GZH");
		order.setPrice(Integer.valueOf(price));
		order.setStatus(OrderStatusEnum.CREATE.getStatus());
		order.setUserId(openId);
		mallOrderRepository.save(order);
		
		OrderPurchase purchase = new OrderPurchase();
		purchase.setOrderId(order.getOrderId());
		purchase.setProductCode(productCode);
		purchase.setPurchaseNumber(1);
		purchase.setUnitPrice(Integer.valueOf(price));
		purchase.setTotalPrice(Integer.valueOf(price)*1);
		orderPurchaseRepository.save(purchase);
		return order;
	}

	@Override
	public PayOrderResponse pay(MallOrder order, String ip) {
		PayOrderRequest payOrderRequest = new PayOrderRequest();
		payOrderRequest.setExpireTime(new Date(new Date().getTime() + 30*60*1000));//30分钟过期
		payOrderRequest.setClientIp(ip);
		payOrderRequest.setPayAmount(order.getPrice());
		payOrderRequest.setPaymentId(order.getOrderId().toString());
		
		JSONObject ob = new JSONObject();
		ob.put("openid", order.getUserId());
		payOrderRequest.setExtraParams(ob.toJSONString());
		return wxPayJsapiService.order(payOrderRequest);
	}


}
