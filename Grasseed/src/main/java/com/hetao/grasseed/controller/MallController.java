package com.hetao.grasseed.controller;



import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hetao.grasseed.model.request.OneProductRequest;
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
	
	/**
	 * 查询商品详情
	 * @param req
	 * @return
	 */
	@PostMapping("/o/productDetail")
	public GrasseedResponse productDetail(@Validated @RequestBody OneProductRequest req){
		log.info("productDetail==="+req);	
		return mallService.productDetail(req);
	}
	
	@RequestMapping("/o/addOrder")
	@ResponseBody
	public void addOrder(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String code = request.getParameter("code");
		String productCode = request.getParameter("productCode");
		String price = request.getParameter("price");
		//String url = URLDecoder.decode(request.getParameter("url"), "UTF-8");
		//url = url+(url.indexOf("?")<0 ?"?":"&")+"code="+code;
		log.info("code:"+code+"==productCode:"+productCode+"==price:"+price);
		//response.sendRedirect(url);
	}
}
