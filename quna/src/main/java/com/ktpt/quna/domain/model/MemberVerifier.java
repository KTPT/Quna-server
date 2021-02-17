package com.ktpt.quna.domain.model;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ktpt.quna.application.dto.LoginRequest;
import com.ktpt.quna.application.dto.LoginResponse;
import com.ktpt.quna.application.dto.MemberCreateRequest;
import com.ktpt.quna.infra.token.JwtTokenProvider;

@Component
public class MemberVerifier {
    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ERROR_MESSAGE = "요청과 일치하는 회원이 존재하지 않습니다.";

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

    public LoginResponse login(LoginRequest request) {
        String nickname = request.getNickname();
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE));

        if (!encoder.matches(request.getPassword(), member.getPassword())) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }

        String token = jwtTokenProvider.createToken(member.getId());

        return LoginResponse.from(token, member);
    }
}
