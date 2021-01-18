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
	private LocalDateTime createdAt;
	private String avatarUrl;

	protected Member() {
	}

	public Member(Long id, String nickname, String password, LocalDateTime createdAt, String avatarUrl) {
		this.id = id;
		this.nickname = nickname;
		this.password = password;
		this.createdAt = createdAt;
		this.avatarUrl = avatarUrl;
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
