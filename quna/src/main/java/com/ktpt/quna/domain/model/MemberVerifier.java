package com.ktpt.quna.domain.model;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ktpt.quna.application.dto.MemberCreateRequest;

@Component
public class MemberVerifier {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    public MemberVerifier(MemberRepository memberRepository, PasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }

    public Member toEntity(MemberCreateRequest request) {
        String nickname = request.getNickname();

        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        return new Member(null, nickname, encoder.encode(request.getPassword()), request.getAvatarUrl(), null);
    }
}
