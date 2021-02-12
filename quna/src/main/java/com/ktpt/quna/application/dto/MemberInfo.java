package com.ktpt.quna.application.dto;

import com.ktpt.quna.domain.model.Member;

public class MemberInfo {

	private final Long id;
	private final String nickname;
	private final String avatarUrl;

	private MemberInfo() {
		this(null, null, null);
	}

	public MemberInfo(Long id, String nickname, String avatarUrl) {
		this.id = id;
		this.nickname = nickname;
		this.avatarUrl = avatarUrl;
	}

	public static MemberInfo from(Member member) {
		return new MemberInfo(member.getId(), member.getNickname(), member.getAvatarUrl());
	}

	public Long getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}
}
