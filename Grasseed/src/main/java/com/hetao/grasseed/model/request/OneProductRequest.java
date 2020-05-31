package com.hetao.grasseed.model.request;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class OneProductRequest {
     @NotNull
	private String productCode;
}
