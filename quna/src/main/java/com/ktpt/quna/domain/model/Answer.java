package com.ktpt.quna.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
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
//        setAuthor(author);
        createdAt = LocalDateTime.now();
        lastModifiedAt = LocalDateTime.now();
    }

    public Member getAuthor() {
        return author;
    }

    public void update(String contents) {
        this.contents = contents;
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

    public void setAuthor(Member author) {
        this.author = author;
        author.getAnswers().add(this);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
}
