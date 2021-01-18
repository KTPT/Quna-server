package com.ktpt.quna.infra.token;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {
	private static final long MEMBER_ID = 1L;
	private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();

	@Test
	void createToken() {
		String actual = jwtTokenProvider.createToken(MEMBER_ID);
		assertThat(actual).isNotEmpty();
	}

	@Test
	void getMemberId() {
		String token = jwtTokenProvider.createToken(MEMBER_ID);
		Long actual = jwtTokenProvider.getMemberId(token);
		assertThat(actual).isEqualTo(MEMBER_ID);
	}
}