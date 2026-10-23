package com.example.exambuilder.web.dto;

import com.example.exambuilder.model.Exam;
import com.example.exambuilder.model.Question;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class ExamForm {
    @NotBlank @Size(max = 120)
    private String title;
    @NotBlank @Size(max = 60)
    private String subject;
    @Valid @Size(min = 1, max = 30)
    private List<QuestionForm> questions = new ArrayList<>();

    public static ExamForm blank() {
        ExamForm form = new ExamForm();
        form.getQuestions().add(new QuestionForm());
        return form;
    }

    public static ExamForm from(Exam exam) {
        ExamForm form = new ExamForm();
        form.setTitle(exam.getTitle());
        form.setSubject(exam.getSubject());
        for (Question q : exam.getQuestions()) {
            QuestionForm item = new QuestionForm();
            item.setText(q.getText());
            item.setChoiceA(q.getChoiceA());
            item.setChoiceB(q.getChoiceB());
            item.setChoiceC(q.getChoiceC());
            item.setChoiceD(q.getChoiceD());
            item.setCorrectAnswer(q.getCorrectAnswer());
            form.getQuestions().add(item);
        }
        return form;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public List<QuestionForm> getQuestions() { return questions; }
    public void setQuestions(List<QuestionForm> questions) { this.questions = questions; }
}
