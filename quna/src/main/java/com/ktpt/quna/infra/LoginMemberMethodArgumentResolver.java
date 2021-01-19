package com.ktpt.quna.infra;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ktpt.quna.application.exception.InvalidTokenException;
import com.ktpt.quna.domain.model.Member;
import com.ktpt.quna.infra.annotation.LoginMember;

@Component
public class LoginMemberMethodArgumentResolver implements HandlerMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LoginMember.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		Member member = (Member)request.getAttribute("loginMember");

		if (Objects.isNull(member)) {
			throw new InvalidTokenException("사용자 정보가 없는 토큰인데 이 예외는 바꿔야할듯합니다.");
		}

		return member;
	}
}
