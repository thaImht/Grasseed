package com.hetao.grasseed.exception;

import lombok.Getter;

public class ApplicationException extends RuntimeException {

    @Getter
    private String code;

    @Getter
    private Throwable rawException;

    public ApplicationException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String code, String message, Throwable rawException) {
        super(message);
        this.code = code;
        this.rawException = rawException;
    }

}
