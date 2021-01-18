package com.ktpt.quna.application.dto;

import com.ktpt.quna.domain.model.Answer;

public class AnswerRequest {
    private final String contents;

    private AnswerRequest() {
        contents = null;
    }

    public AnswerRequest(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public Answer toEntity(Long questionId) {
        return new Answer(null, questionId, contents, null, null, null);
    }
}
