package com.ktpt.quna.application.dto;

import com.ktpt.quna.domain.model.Member;

public class TokenResponse {
    private static final String BEARER = "bearer";

    private final String type;
    private final String token;
    private final MemberInfo member;

    private TokenResponse() {
        this(null, null, null);
    }

    public TokenResponse(String type, String token, MemberInfo member) {
        this.type = type;
        this.token = token;
        this.member = member;
    }

    public static TokenResponse from(String token, Member member) {
        return new TokenResponse(BEARER, token, MemberInfo.from(member));
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public MemberInfo getMember() {
        return member;
    }
}
