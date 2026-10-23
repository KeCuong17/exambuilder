package com.example.exambuilder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/** Only the selected pages' extracted text is saved, not the original PDF bytes. */
@Entity
@Table(name = "uploaded_documents")
public class UploadedDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount owner;

    @Column(nullable = false, length = 200)
    private String filename;

    @Column(nullable = false, length = 120)
    private String chapter;

    @Column(nullable = false)
    private int startPage;
    @Column(nullable = false)
    private int endPage;
    @Column(nullable = false)
    private int totalPages;
    @Column(nullable = false)
    private int questionCount;
    @Column(nullable = false, length = 20)
    private String difficulty;
    @Column(nullable = false, length = 24)
    private String questionType;

    // TEXT maps correctly to PostgreSQL text as well as H2.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String extractedText;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public UserAccount getOwner() { return owner; }
    public void setOwner(UserAccount owner) { this.owner = owner; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getChapter() { return chapter; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    public int getStartPage() { return startPage; }
    public void setStartPage(int startPage) { this.startPage = startPage; }
    public int getEndPage() { return endPage; }
    public void setEndPage(int endPage) { this.endPage = endPage; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
