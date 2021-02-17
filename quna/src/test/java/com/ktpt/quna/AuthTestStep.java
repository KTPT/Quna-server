package com.ktpt.quna;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ktpt.quna.domain.model.Member;
import com.ktpt.quna.domain.model.MemberRepository;
import com.ktpt.quna.infra.token.JwtTokenProvider;

@Component
public class AuthTestStep {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public Member createDefaultMember() {
        return memberRepository.save(new Member(null, "승완", "123", "haha", LocalDateTime.now()));
    }

    public String createToken(Long memberId) {
        String jwt = jwtTokenProvider.createToken(memberId);
        return "Bearer " + jwt;
    }

    public void clearMember() {
        memberRepository.deleteAll();
    }
}
