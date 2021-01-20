package com.ktpt.quna.infra;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ktpt.quna.application.exception.InvalidTokenException;
import com.ktpt.quna.domain.model.MemberRepository;
import com.ktpt.quna.infra.token.JwtTokenProvider;
import com.ktpt.quna.infra.token.TokenExtractor;

@Component
public class LoginMemberInterceptor implements HandlerInterceptor {
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenExtractor tokenExtractor;
	private final MemberRepository repository;

	public LoginMemberInterceptor(JwtTokenProvider jwtTokenProvider,
		TokenExtractor tokenExtractor, MemberRepository repository) {
		this.jwtTokenProvider = jwtTokenProvider;
		this.tokenExtractor = tokenExtractor;
		this.repository = repository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorization == null) {
			throw new InvalidTokenException("이것도 바꿔야 한다.");
		}

		String token = tokenExtractor.extract(request, HttpHeaders.AUTHORIZATION, "bearer");

		if (token.isEmpty()) {
			return true;
		}

		Long memberId = jwtTokenProvider.getMemberId(token);
		if (!repository.existsById(memberId)) {
			throw new InvalidTokenException("유효하지 않은 토큰입니다.");
		}

		request.setAttribute("memberId", memberId);
		return true;
	}
}
