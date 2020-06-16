package com.hetao.grasseed.controller;



import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hetao.grasseed.common.https.HttpsUtils;
import com.hetao.grasseed.common.util.IPUtil;
import com.hetao.grasseed.common.util.WxUtil;
import com.hetao.grasseed.dao.MallOrderRepository;
import com.hetao.grasseed.dao.ProductRepository;
import com.hetao.grasseed.model.entity.MallOrder;
import com.hetao.grasseed.model.entity.Product;
import com.hetao.grasseed.model.enumeration.OrderStatusEnum;
import com.hetao.grasseed.model.request.OneProductRequest;
import com.hetao.grasseed.model.response.GrasseedResponse;
import com.hetao.grasseed.model.response.PayOrderResponse;
import com.hetao.grasseed.service.MallService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/mall")
@Slf4j
@EnableJpaAuditing
public class MallController {

	@Autowired
	private MallService mallService;
	
	@Autowired
	private MallOrderRepository mallOrderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private WxUtil wxUtil;
	/**
	 * 查询商品列表
	 * @param req
	 * @return
	 */
	@PostMapping("/o/productList")
	@ResponseBody
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
	@ResponseBody
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
		
		Product product = productRepository.findByProductCode(productCode);
		if(!product.getPrice().equals(Integer.valueOf(price))) {
			response.sendRedirect("/errorPage");
		}
		String openId = wxUtil.getOpenIdOfUser(code);
		log.info("code:"+code+"==productCode:"+productCode+"==price:"+price+"==openId:"+openId);
		
		MallOrder order = mallService.addOrder(openId,productCode,price);
		
		String ip = IPUtil.getRemoteIP(request);
		PayOrderResponse payOrderResponse = mallService.pay(order,ip);
		
		order.setStatus(OrderStatusEnum.PAYING.getStatus());
		order.setReturnUrl(payOrderResponse.getValue());
		mallOrderRepository.save(order);
		
		response.sendRedirect("/payPage?orderId="+payOrderResponse.getOrderId()+"&value="+payOrderResponse.getValue());
	}
}
