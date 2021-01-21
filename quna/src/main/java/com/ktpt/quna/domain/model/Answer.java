package com.ktpt.quna.domain.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Answer {
    private static final String UPDATE_FAIL_MESSAGE = "동일한 내용으로 수정할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long questionId;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    protected Answer() {
    }

    public Answer(Long id, Long questionId, String contents, Member author, LocalDateTime createdAt,
        LocalDateTime lastModifiedAt) {
        this.id = id;
        this.questionId = questionId;
        this.contents = contents;
        this.author = author;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public void create() {
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
    }

    public void update(String contents) {
        verify(contents);
        this.contents = contents;
        lastModifiedAt = LocalDateTime.now();
    }

    private void verify(String contents) {
        if (this.contents.equals(contents)) {
            throw new IllegalArgumentException(UPDATE_FAIL_MESSAGE);
        }
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

    public Member getAuthor() {
        return author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
}
