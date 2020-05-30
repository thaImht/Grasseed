package com.hetao.grasseed.model.enumeration;

import lombok.Getter;

@Getter
public enum ResponseEnum {
	SUCCESS("G00000","成功"), 
	PARAMERROR("G00001","请求参数不正确。"),

    NO_PERMISSION_ERROR("G99998","没有权限。"),
    UNCAUGHT_EXCEPTION("G99999","未知错误");

    private String code;
    private String msg;
    private ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
