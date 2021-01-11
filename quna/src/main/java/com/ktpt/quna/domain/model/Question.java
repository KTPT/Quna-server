package com.ktpt.quna.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Question {
    private static final String UPDATE_FAIL_MESSAGE = "동일한 내용으로 수정할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String contents;
    private Long responderId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    protected Question() {
    }

    public Question(Long id, String title, String contents, Long responderId,
            LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.responderId = responderId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public Question create() {
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
        return this;
    }

    public Question update(String title, String contents, Long responderId) {
        verify(title, contents, responderId);
        this.title = title;
        this.contents = contents;
        this.responderId = responderId;
        this.lastModifiedAt = LocalDateTime.now();
        return this;
    }

    private void verify(String title, String contents, Long responderId) {
        if (this.responderId == null && responderId == null && isEqual(title, contents)) {
            throw new IllegalArgumentException(UPDATE_FAIL_MESSAGE);
        }
        if (isEqual(title, contents) && this.responderId.equals(responderId)) {
            throw new IllegalArgumentException(UPDATE_FAIL_MESSAGE);
        }
    }

    private boolean isEqual(String title, String contents) {
        return this.title.equals(title) && this.contents.equals(contents);
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

    public Long getResponderId() {
        return responderId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
}
