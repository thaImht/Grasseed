package com.hetao.grasseed.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hetao.grasseed.model.response.GrasseedResponse;
import com.hetao.grasseed.service.MallService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/mall")
@Slf4j
@EnableJpaAuditing
public class MallController {

	@Autowired
	private MallService mallService;
	
	/**
	 * 查询商品列表
	 * @param req
	 * @return
	 */
	@PostMapping("/o/productList")
	public GrasseedResponse productList(){
		log.info("productList===");	
		return mallService.productList();
	}
	
}
