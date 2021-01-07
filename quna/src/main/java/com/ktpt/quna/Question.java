package com.ktpt.quna;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Question {
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
