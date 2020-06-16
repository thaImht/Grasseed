package com.hetao.grasseed.integration.exception;

public class IntegrationException extends Exception {

    public IntegrationException() {
        this((String)null);
    }

    public IntegrationException(Throwable cause) {
        this( null , cause);
    }

    public IntegrationException(String msg) {
        super(msg == null? "" : msg);
    }

    public IntegrationException(String msg, Throwable cause) {
        super(msg == null? "" : msg, cause);
    }

}
