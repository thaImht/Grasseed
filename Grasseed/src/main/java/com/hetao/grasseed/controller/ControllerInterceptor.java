package com.hetao.grasseed.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.hetao.grasseed.model.response.GrasseedResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerInterceptor {

    @Around("execution(com.hetao.grasseed.model.response.GrasseedResponse com.tvguo.controller.*Controller.*(..))")
    public GrasseedResponse intercept(ProceedingJoinPoint pjp) throws Throwable {

        return (GrasseedResponse)pjp.proceed();
    }


}
