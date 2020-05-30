package com.hetao.grasseed.model.response;

import com.hetao.grasseed.model.enumeration.ResponseEnum;

public class GrasseedResponseBuilder {

    public static GrasseedResponse buildSuccssResponse(Object data) {
        GrasseedResponse response = buildResponse(ResponseEnum.SUCCESS);
        response.setData(data);
        return response;
    }

    public static GrasseedResponse buildResponse(ResponseEnum responseEnum) {
        GrasseedResponse response = new GrasseedResponse();
        response.setCode(responseEnum.getCode());
        response.setMsg(responseEnum.getMsg());
        return response;
    }

    public static GrasseedResponse buildResponse(String code, String msg) {
        GrasseedResponse response = new GrasseedResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static GrasseedResponse buildResponse(ResponseEnum responseEnum, String extraMsg) {
        return buildResponse(responseEnum.getCode(), responseEnum.getMsg() + " " + extraMsg);
    }

	public static GrasseedResponse buildResponse(ResponseEnum responseEnum, Object data) {
		GrasseedResponse response = new GrasseedResponse();
        response.setCode(responseEnum.getCode());
        response.setMsg(responseEnum.getMsg());
        response.setData(data);
        return response;
	}

}
