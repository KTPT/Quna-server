package com.ktpt.quna.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ktpt.quna.domain.model.Question;

public class QuestionResponse {
    private final Long id;
    private final String title;
    private final String contents;
    private final MemberResponse author;
    private final Long responderId;
    private final String createdAt;
    private final String lastModifiedAt;

    private QuestionResponse() {
        this(null, null, null, null, null, null, null);
    }

    public QuestionResponse(Long id, String title, String contents, MemberResponse author, Long responderId,
        String createdAt, String lastModifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.responderId = responderId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static QuestionResponse from(Question question) {
        return new QuestionResponse(question.getId(), question.getTitle(), question.getContents(),
            MemberResponse.from(question.getAuthor()), question.getResponderId(), question.getCreatedAt().toString(),
            question.getLastModifiedAt().toString());
    }

    public static List<QuestionResponse> listOf(List<Question> questions) {
        return questions.stream()
            .map(QuestionResponse::from)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public MemberResponse getAuthor() {
        return author;
    }

    public Long getResponderId() {
        return responderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }
}
