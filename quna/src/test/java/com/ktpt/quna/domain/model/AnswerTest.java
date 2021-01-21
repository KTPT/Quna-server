package com.ktpt.quna.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswerTest {
    private Answer answer;
    private String contents;

    @BeforeEach
    void setUp() {
        contents = "contents";
        answer = new Answer(1L, 1L, contents, null, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void update() {
        assertThatThrownBy(() -> answer.update(contents))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 내용으로 수정할 수 없습니다.");
    }
}