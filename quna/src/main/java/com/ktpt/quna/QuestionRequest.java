package com.ktpt.quna;

public class QuestionRequest {
    private final String title;
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
