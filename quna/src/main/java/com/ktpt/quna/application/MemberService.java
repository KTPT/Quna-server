package com.ktpt.quna.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ktpt.quna.application.dto.LoginRequest;
import com.ktpt.quna.application.dto.MemberCreateRequest;
import com.ktpt.quna.application.dto.MemberResponse;
import com.ktpt.quna.application.dto.TokenResponse;
import com.ktpt.quna.domain.model.Member;
import com.ktpt.quna.domain.model.MemberRepository;
import com.ktpt.quna.domain.model.MemberVerifier;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberVerifier memberVerifier;

    public MemberService(MemberRepository memberRepository, MemberVerifier memberVerifier) {
        this.memberRepository = memberRepository;
        this.memberVerifier = memberVerifier;
    }

    @Transactional
    public MemberResponse create(MemberCreateRequest request) {
        Member member = memberVerifier.toEntity(request);
        Member saved = memberRepository.save(member.create());
        return MemberResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        String token = memberVerifier.getToken(request);
        return TokenResponse.from(token);
    }
}
