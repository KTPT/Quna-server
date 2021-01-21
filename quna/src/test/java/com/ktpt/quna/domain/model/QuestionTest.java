package com.ktpt.quna.domain.model;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionTest {

    private Question question;
    private String title;
    private String contents;
    private Long responderId;

    @BeforeEach
    void setUp() {
        title = "title";
        contents = "contents";
        responderId = 1L;
        question = new Question(1L, title, contents, null, responderId, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void update() {
        assertThatThrownBy(() -> question.update(title, contents, responderId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("동일한 내용으로 수정할 수 없습니다.");

        Question noResponder = new Question(1L, title, contents, null, null, LocalDateTime.now(), LocalDateTime.now());
        assertThatThrownBy(() -> noResponder.update(title, contents, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("동일한 내용으로 수정할 수 없습니다.");
    }
}