package com.ktpt.quna.application.dto;

public class TokenResponse {
    private static final String BEARER = "bearer";

    private final String type;
    private final String token;

    private TokenResponse() {
        this(null, null);
    }

    public TokenResponse(String type, String token) {
        this.type = type;
        this.token = token;
    }

    public static TokenResponse from(String token) {
        return new TokenResponse(BEARER, token);
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }
}
