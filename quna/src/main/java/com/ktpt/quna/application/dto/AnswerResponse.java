package com.ktpt.quna.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ktpt.quna.domain.model.Answer;

public class AnswerResponse {
    private final Long id;
    private final Long questionId;
    private final String contents;
    private final String createdAt;
    private final String lastModifiedAt;

    private AnswerResponse() {
        id = null;
        questionId = null;
        contents = null;
        createdAt = null;
        lastModifiedAt = null;
    }

    public AnswerResponse(Long id, Long questionId, String contents, String createdAt, String lastModifiedAt) {
        this.id = id;
        this.questionId = questionId;
        this.contents = contents;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static AnswerResponse from(Answer answer) {
        return new AnswerResponse(answer.getId(), answer.getQuestionId(), answer.getContents(),
            answer.getCreatedAt().toString(), answer.getLastModifiedAt().toString());
    }

    public static List<AnswerResponse> listOf(List<Answer> answers) {
        return answers.stream()
            .map(AnswerResponse::from)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getContents() {
        return contents;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }
}
