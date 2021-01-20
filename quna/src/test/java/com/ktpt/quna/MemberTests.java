package com.ktpt.quna;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktpt.quna.application.dto.MemberCreateRequest;
import com.ktpt.quna.application.dto.MemberResponse;
import com.ktpt.quna.application.exception.ErrorResponse;
import com.ktpt.quna.domain.model.Member;
import com.ktpt.quna.domain.model.MemberRepository;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class MemberTests {
    @Autowired
    private MemberRepository memberRepository;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void create() throws Exception {
        String nickname = "nickname";
        String password = "password";
        String avatarUrl = "avatarUrl";

        MemberCreateRequest request = new MemberCreateRequest(nickname, password, avatarUrl);

        MvcResult mvcResult = mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(header().string(LOCATION, matchesRegex("/members/\\d*")))
                .andExpect(status().isCreated())
                .andReturn();

        MemberResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                MemberResponse.class);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getNickname()).isEqualTo(nickname);
        assertThat(encoder.matches(password, response.getPassword())).isTrue();
        assertThat(response.getAvatarUrl()).isEqualTo(avatarUrl);
    }

    @Test
    void create_WhenDuplicatedNickname_ThenThrowException() throws Exception {
        String nickname = "nickname";
        String password = "password";
        String avatarUrl = "avatarUrl";
        memberRepository.save(new Member(null, nickname, encoder.encode(password), avatarUrl, null).create());

        MemberCreateRequest request = new MemberCreateRequest(nickname, password, avatarUrl);

        MvcResult mvcResult = mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                ErrorResponse.class);

        assertThat(response.getMessage()).isEqualTo("중복된 닉네임입니다.");
    }
}
