package com.hetao.grasseed.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hetao.grasseed.dao.ProductRepository;
import com.hetao.grasseed.model.entity.Product;
import com.hetao.grasseed.model.response.GrasseedResponse;
import com.hetao.grasseed.model.response.GrasseedResponseBuilder;
import com.hetao.grasseed.service.MallService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MallServiceImpl implements MallService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public GrasseedResponse productList() {
		List<Product> products = productRepository.findByIsOnlineTrue();
		return GrasseedResponseBuilder.buildSuccssResponse(products);
	}

}
