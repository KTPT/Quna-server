package com.ktpt.quna.application.dto;

import javax.validation.constraints.NotBlank;

import com.ktpt.quna.domain.model.Question;

public class QuestionRequest {
    @NotBlank
    private final String title;
    @NotBlank
    private final String contents;
    private final Long responderId;

    private QuestionRequest() {
        title = null;
        contents = null;
        responderId = null;
    }

    public QuestionRequest(String title, String contents, Long responderId) {
        this.title = title;
        this.contents = contents;
        this.responderId = responderId;
    }

    public Question toEntity() {
        return new Question(null, title, contents, null, responderId, null, null);
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Long getResponderId() {
        return responderId;
    }
}
