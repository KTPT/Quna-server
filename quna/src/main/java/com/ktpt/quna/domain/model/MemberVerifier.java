package com.ktpt.quna.domain.model;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ktpt.quna.application.dto.LoginRequest;
import com.ktpt.quna.application.dto.MemberCreateRequest;
import com.ktpt.quna.infra.token.JwtTokenProvider;

@Component
public class MemberVerifier {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberVerifier(MemberRepository memberRepository, PasswordEncoder encoder,
            JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Member toEntity(MemberCreateRequest request) {
        String nickname = request.getNickname();

        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        return new Member(null, nickname, encoder.encode(request.getPassword()), request.getAvatarUrl(), null);
    }

    public String getToken(LoginRequest request) {
        String errorMessage = "요청과 일치하는 회원이 존재하지 않습니다.";

        Member member = memberRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));

        if (!encoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException(errorMessage);
        }

        return jwtTokenProvider.createToken(member.getId());
    }
}
