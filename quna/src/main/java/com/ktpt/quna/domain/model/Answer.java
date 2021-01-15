package com.ktpt.quna.domain.model;

import com.ktpt.quna.application.dto.AnswerRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long questionId;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    protected Answer() {
    }

    public Answer(Long id, Long questionId, String contents, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.questionId = questionId;
        this.contents = contents;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public void create() {
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
    }

    public void update(AnswerRequest request) {
        contents = request.getContents();
        lastModifiedAt = LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
}
