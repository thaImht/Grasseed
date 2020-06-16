package com.hetao.grasseed.service;

import com.hetao.grasseed.model.entity.MallOrder;
import com.hetao.grasseed.model.request.OneProductRequest;
import com.hetao.grasseed.model.response.GrasseedResponse;
import com.hetao.grasseed.model.response.PayOrderResponse;

public interface MallService {

	GrasseedResponse productList();

	GrasseedResponse productDetail(OneProductRequest req);

	MallOrder addOrder(String openId, String productCode, String price);

	PayOrderResponse pay(MallOrder order, String ip);

}
