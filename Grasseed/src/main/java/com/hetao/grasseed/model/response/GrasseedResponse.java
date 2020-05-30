package com.hetao.grasseed.model.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GrasseedResponse {
	private String code;
	private String msg;
	private Object data = "";
}
