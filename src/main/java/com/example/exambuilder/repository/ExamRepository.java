package com.example.exambuilder.repository;

import com.example.exambuilder.model.Exam;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByOwner_UsernameOrderByCreatedAtDesc(String username);

    @EntityGraph(attributePaths = "questions")
    Optional<Exam> findByIdAndOwner_Username(Long id, String username);
}
