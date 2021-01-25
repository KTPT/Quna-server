package com.ktpt.quna.application.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private final String nickname;
    @NotBlank
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
