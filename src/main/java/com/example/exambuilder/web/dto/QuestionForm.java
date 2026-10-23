package com.example.exambuilder.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class QuestionForm {
    @NotBlank @Size(max = 1000)
    private String text;
    @NotBlank @Size(max = 255)
    private String choiceA;
    @NotBlank @Size(max = 255)
    private String choiceB;
    @NotBlank @Size(max = 255)
    private String choiceC;
    @NotBlank @Size(max = 255)
    private String choiceD;
    @NotBlank @Pattern(regexp = "[ABCD]")
    private String correctAnswer = "A";

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
