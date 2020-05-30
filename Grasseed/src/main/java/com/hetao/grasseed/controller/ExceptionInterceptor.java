package com.hetao.grasseed.controller;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hetao.grasseed.exception.ApplicationException;
import com.hetao.grasseed.model.enumeration.ResponseEnum;
import com.hetao.grasseed.model.response.GrasseedResponse;
import com.hetao.grasseed.model.response.GrasseedResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GrasseedResponse handle(MethodArgumentNotValidException notValidException) {
        String errorString = notValidException.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return GrasseedResponseBuilder.buildResponse(
                ResponseEnum.PARAMERROR.getCode(),
                ResponseEnum.PARAMERROR.getMsg() + errorString
        );
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public GrasseedResponse handle(BindException bindException) {

        log.error("系统发生参数错误: {}", getStackTrace(bindException));

        return GrasseedResponseBuilder.buildResponse(
                ResponseEnum.PARAMERROR.getCode(),
                ResponseEnum.PARAMERROR.getMsg() + bindException.getFieldError().getDefaultMessage()
        );
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public GrasseedResponse handle(ApplicationException exception) {
        log.error(exception.getMessage());
        return GrasseedResponseBuilder.buildResponse(
                exception.getCode(),
                exception.getMessage()
        );
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public GrasseedResponse handle(Throwable throwable) {
        log.error("系统发生未知错误: {}", getStackTrace(throwable));
        return GrasseedResponseBuilder.buildResponse(ResponseEnum.UNCAUGHT_EXCEPTION);
    }

    private String getStackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

}
