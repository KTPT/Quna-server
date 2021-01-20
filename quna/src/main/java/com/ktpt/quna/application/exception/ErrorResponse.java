package com.ktpt.quna.application.exception;

public class ErrorResponse {
    private final String message;

    private ErrorResponse() {
        message = null;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
