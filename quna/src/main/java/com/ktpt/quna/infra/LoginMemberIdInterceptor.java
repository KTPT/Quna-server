package com.ktpt.quna.infra;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ktpt.quna.application.exception.InvalidTokenException;
import com.ktpt.quna.domain.model.MemberRepository;
import com.ktpt.quna.infra.annotation.LoginMemberId;
import com.ktpt.quna.infra.token.JwtTokenProvider;
import com.ktpt.quna.infra.token.TokenExtractor;

@Component
public class LoginMemberIdInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;
    private final MemberRepository repository;

    public LoginMemberIdInterceptor(JwtTokenProvider jwtTokenProvider,
            TokenExtractor tokenExtractor, MemberRepository repository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExtractor = tokenExtractor;
        this.repository = repository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Optional<LoginMemberId> annotation = getParameterAnnotation((HandlerMethod)handler, LoginMemberId.class);
        if (annotation.isEmpty()) {
            return true;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new InvalidTokenException("유효한 토큰을 요청과 함께 보내야합니다.");
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

    private <A extends Annotation> Optional<A> getParameterAnnotation(HandlerMethod handlerMethod,
            Class<A> annotationType) {
        return Arrays.stream(handlerMethod.getMethodParameters())
                .filter(parameter -> parameter.hasParameterAnnotation(annotationType))
                .map(parameter -> parameter.getParameterAnnotation(annotationType))
                .findFirst();
    }
}