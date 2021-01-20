package com.ktpt.quna.application.dto;

public class LoginRequest {
    private final String nickname;
    private final String password;

    private LoginRequest() {
        this(null, null);
    }

    public LoginRequest(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }
}
