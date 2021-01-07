package com.ktpt.quna;

public class QuestionResponse {
    private final Long id;
    private final String title;
    private final String contents;
    private final Long responderId;
    private final String createdAt;
    private final String lastModifiedAt;

    private QuestionResponse() {
        id = null;
        title = null;
        contents = null;
        responderId = null;
        createdAt = null;
        lastModifiedAt = null;
    }

    public QuestionResponse(Long id, String title, String contents, Long responderId,
            String createdAt, String lastModifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.responderId = responderId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getLastModifiedAt() {
        return lastModifiedAt;
    }
}
