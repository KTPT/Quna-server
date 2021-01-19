package com.ktpt.quna.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ktpt.quna.infra.LoginMemberInterceptor;
import com.ktpt.quna.infra.LoginMemberMethodArgumentResolver;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
	private final LoginMemberInterceptor loginMemberInterceptor;
	private final LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

	public AuthConfig(LoginMemberInterceptor loginMemberInterceptor,
		LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver) {
		this.loginMemberInterceptor = loginMemberInterceptor;
		this.loginMemberMethodArgumentResolver = loginMemberMethodArgumentResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginMemberInterceptor);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginMemberMethodArgumentResolver);
	}
}
