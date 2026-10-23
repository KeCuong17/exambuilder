package com.example.exambuilder.service;

import com.example.exambuilder.model.Exam;
import com.example.exambuilder.model.Question;
import com.example.exambuilder.model.UserAccount;
import com.example.exambuilder.repository.ExamRepository;
import com.example.exambuilder.repository.UserAccountRepository;
import com.example.exambuilder.web.dto.ExamForm;
import com.example.exambuilder.web.dto.QuestionForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ExamService {
    private final ExamRepository exams;
    private final UserAccountRepository users;

    public ExamService(ExamRepository exams, UserAccountRepository users) {
        this.exams = exams;
        this.users = users;
    }

    @Transactional(readOnly = true)
    public List<Exam> listMine(String username) {
        return exams.findByOwner_UsernameOrderByCreatedAtDesc(username);
    }

    @Transactional(readOnly = true)
    public Exam getMine(Long examId, String username) {
        return exams.findByIdAndOwner_Username(examId, username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Exam create(String username, ExamForm form) {
        UserAccount owner = users.findByUsername(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        Exam exam = new Exam();
        exam.setOwner(owner);
        fill(exam, form);
        return exams.save(exam);
    }

    @Transactional
    public void update(Long id, String username, ExamForm form) {
        Exam exam = getMine(id, username);
        fill(exam, form);
    }

    @Transactional
    public void delete(Long id, String username) {
        Exam exam = getMine(id, username);
        exams.delete(exam);
    }

    private void fill(Exam exam, ExamForm form) {
        exam.setTitle(form.getTitle().trim());
        exam.setSubject(form.getSubject().trim());
        exam.getQuestions().clear();
        int position = 1;
        for (QuestionForm input : form.getQuestions()) {
            Question q = new Question();
            q.setPosition(position++);
            q.setExam(exam);
            q.setText(input.getText().trim());
            q.setChoiceA(input.getChoiceA().trim());
            q.setChoiceB(input.getChoiceB().trim());
            q.setChoiceC(input.getChoiceC().trim());
            q.setChoiceD(input.getChoiceD().trim());
            q.setCorrectAnswer(input.getCorrectAnswer());
            exam.getQuestions().add(q);
        }
    }
}
