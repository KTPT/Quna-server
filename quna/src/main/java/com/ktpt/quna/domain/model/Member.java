package com.ktpt.quna.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String password;
    private String avatarUrl;
    private LocalDateTime createdAt;

    protected Member() {
    }

    public Member(Long id, String nickname, String password, String avatarUrl, LocalDateTime createdAt) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    public Member create() {
        createdAt = LocalDateTime.now();
        return this;
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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
