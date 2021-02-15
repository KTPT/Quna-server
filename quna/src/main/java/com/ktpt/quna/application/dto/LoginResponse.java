package com.ktpt.quna.application.dto;

import com.ktpt.quna.domain.model.Member;

public class LoginResponse {
    private static final String BEARER = "bearer";

    private final String type;
    private final String token;
    private final MemberResponse member;

    private LoginResponse() {
        this(null, null, null);
    }

    public LoginResponse(String type, String token, MemberResponse member) {
        this.type = type;
        this.token = token;
        this.member = member;
    }

    public static LoginResponse from(String token, Member member) {
        return new LoginResponse(BEARER, token, MemberResponse.from(member));
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public MemberResponse getMember() {
        return member;
    }
}
