package com.ktpt.quna.presentation.verifier;

public class InvalidRequestException extends IllegalArgumentException {
    public InvalidRequestException(String s) {
        super(s);
    }
}
