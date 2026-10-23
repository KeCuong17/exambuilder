package com.example.exambuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_questions")
public class AiQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ai_exam_id", nullable = false)
    private AiExam exam;

    @Column(nullable = false)
    private int position;
    @Column(nullable = false, length = 8)
    private String type; // MCQ or ESSAY
    @Column(nullable = false, length = 16)
    private String difficulty;
    @Column(nullable = false, length = 2500)
    private String question;
    @Column(length = 600)
    private String optionA;
    @Column(length = 600)
    private String optionB;
    @Column(length = 600)
    private String optionC;
    @Column(length = 600)
    private String optionD;
    @Column(length = 1)
    private String correctAnswer;
    @Column(nullable = false, length = 4000)
    private String explanation;
    @Column(nullable = false)
    private int sourcePage;

    public Long getId() { return id; }
    public AiExam getExam() { return exam; }
    public void setExam(AiExam exam) { this.exam = exam; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public int getSourcePage() { return sourcePage; }
    public void setSourcePage(int sourcePage) { this.sourcePage = sourcePage; }
}
