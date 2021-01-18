package com.ktpt.quna.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String nickname;
    private String password;
    private String avatarUrl;

    @OneToMany(mappedBy = "author")
    private List<Answer> answers = new ArrayList<>();

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

    public List<Answer> getAnswers() {
        return answers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
