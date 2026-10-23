package com.example.exambuilder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** AI-generated exams are stored separately, to avoid changing existing MCQ data. */
@Entity
@Table(name = "ai_exams")
public class AiExam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserAccount owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private UploadedDocument document;

    @Column(nullable = false, length = 160)
    private String title;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<AiQuestion> questions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public UserAccount getOwner() { return owner; }
    public void setOwner(UserAccount owner) { this.owner = owner; }
    public UploadedDocument getDocument() { return document; }
    public void setDocument(UploadedDocument document) { this.document = document; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<AiQuestion> getQuestions() { return questions; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
