package com.hetao.grasseed.service;

import com.hetao.grasseed.model.request.OneProductRequest;
import com.hetao.grasseed.model.response.GrasseedResponse;

public interface MallService {

	GrasseedResponse productList();

	GrasseedResponse productDetail(OneProductRequest req);

}
