package com.example.exambuilder.repository;

import com.example.exambuilder.model.UploadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UploadedDocumentRepository extends JpaRepository<UploadedDocument, Long> {
    List<UploadedDocument> findByOwner_UsernameOrderByCreatedAtDesc(String username);
    Optional<UploadedDocument> findByIdAndOwner_Username(Long id, String username);
}
