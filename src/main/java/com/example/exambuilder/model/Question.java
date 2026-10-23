package com.example.exambuilder.model;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @Column(nullable = false)
    private int position;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(nullable = false, length = 255)
    private String choiceA;
    @Column(nullable = false, length = 255)
    private String choiceB;
    @Column(nullable = false, length = 255)
    private String choiceC;
    @Column(nullable = false, length = 255)
    private String choiceD;

    @Column(nullable = false, length = 1)
    private String correctAnswer;

    public Long getId() { return id; }
    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getChoiceA() { return choiceA; }
    public void setChoiceA(String choiceA) { this.choiceA = choiceA; }
    public String getChoiceB() { return choiceB; }
    public void setChoiceB(String choiceB) { this.choiceB = choiceB; }
    public String getChoiceC() { return choiceC; }
    public void setChoiceC(String choiceC) { this.choiceC = choiceC; }
    public String getChoiceD() { return choiceD; }
    public void setChoiceD(String choiceD) { this.choiceD = choiceD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}
