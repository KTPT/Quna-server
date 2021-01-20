package com.ktpt.quna.application.dto;

public class MemberCreateRequest {
    private final String nickname;
    private final String password;
    private final String avatarUrl;

    private MemberCreateRequest() {
        this(null, null, null);
    }

    public MemberCreateRequest(String nickname, String password, String avatarUrl) {
        this.nickname = nickname;
        this.password = password;
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
