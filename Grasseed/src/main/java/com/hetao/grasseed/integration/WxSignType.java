package com.hetao.grasseed.integration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public enum WxSignType {
    MD5("MD5"), HMACSHA256("HMAC-SHA256");

    @Getter
    private String name;

    WxSignType(String name) {
        this.name = name;
    }

    public static Optional<WxSignType> getSignType(String name) {
        return Arrays.stream(WxSignType.values()).filter(signType -> Objects.equals(signType.getName(), name)).findAny();
    }
}