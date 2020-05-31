package com.hetao.grasseed.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/")
@Slf4j
public class PageController {
	
	@GetMapping("productList")
    public ModelAndView productList() {
		log.info("productList html===");	
        return new ModelAndView("productList");
    }
	
	@GetMapping("productDetail")
    public ModelAndView productDetail() {
		log.info("productDetail html===");	
        return new ModelAndView("productDetail");
    }
}
