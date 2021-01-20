package com.ktpt.quna.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ktpt.quna.infra.LoginMemberIdInterceptor;
import com.ktpt.quna.infra.LoginMemberMethodArgumentResolver;
import com.ktpt.quna.infra.LoginRequiredInterceptor;

@Configuration
public class AuthConfig implements WebMvcConfigurer {
	private final LoginRequiredInterceptor loginRequiredInterceptor;
	private final LoginMemberIdInterceptor loginMemberIdInterceptor;
	private final LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver;

	public AuthConfig(LoginRequiredInterceptor loginRequiredInterceptor,
		LoginMemberIdInterceptor loginMemberIdInterceptor,
		LoginMemberMethodArgumentResolver loginMemberMethodArgumentResolver) {
		this.loginRequiredInterceptor = loginRequiredInterceptor;
		this.loginMemberIdInterceptor = loginMemberIdInterceptor;
		this.loginMemberMethodArgumentResolver = loginMemberMethodArgumentResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginRequiredInterceptor).order(0);
		registry.addInterceptor(loginMemberIdInterceptor).order(1);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginMemberMethodArgumentResolver);
	}
}
