package com.example.exambuilder.repository;

import com.example.exambuilder.model.AiExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.List;
import java.util.Optional;

public interface AiExamRepository extends JpaRepository<AiExam, Long> {
    @EntityGraph(attributePaths = {"document", "questions"})
    List<AiExam> findByOwner_UsernameOrderByCreatedAtDesc(String username);
    @EntityGraph(attributePaths = {"document", "questions"})
    Optional<AiExam> findByIdAndOwner_Username(Long id, String username);
    long countByOwner_UsernameAndCreatedAtAfter(String username, java.time.LocalDateTime after);
    long countByCreatedAtAfter(java.time.LocalDateTime after);
}
