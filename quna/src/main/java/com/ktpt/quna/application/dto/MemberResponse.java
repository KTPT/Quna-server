package com.ktpt.quna.application.dto;

import java.time.LocalDateTime;

import com.ktpt.quna.domain.model.Member;

public class MemberResponse {
    private final Long id;
    private final String nickname;
    private final String password;
    private final LocalDateTime createdAt;
    private final String avatarUrl;

    private MemberResponse() {
        this(null, null, null, null, null);
    }

    public MemberResponse(Long id, String nickname, String password, LocalDateTime createdAt, String avatarUrl) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.createdAt = createdAt;
        this.avatarUrl = avatarUrl;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getPassword(), member.getCreatedAt(),
            member.getAvatarUrl());
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
