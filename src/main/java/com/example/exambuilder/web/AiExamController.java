package com.example.exambuilder.web;

import com.example.exambuilder.model.AiExam;
import com.example.exambuilder.model.UploadedDocument;
import com.example.exambuilder.repository.AiExamRepository;
import com.example.exambuilder.repository.UploadedDocumentRepository;
import com.example.exambuilder.service.AiExamService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
public class AiExamController {
    private final AiExamService aiService;
    private final AiExamRepository aiExams;
    private final UploadedDocumentRepository documents;

    public AiExamController(AiExamService aiService, AiExamRepository aiExams,
                            UploadedDocumentRepository documents) {
        this.aiService = aiService;
        this.aiExams = aiExams;
        this.documents = documents;
    }

    @PostMapping("/documents/{id}/generate")
    public String generate(@PathVariable Long id, Principal principal, Model model) {
        UploadedDocument doc = documents.findByIdAndOwner_Username(id, principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            AiExam generated = aiService.generate(principal.getName(), doc);
            return "redirect:/ai-exams/" + generated.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("document", doc);
            String text = doc.getExtractedText();
            model.addAttribute("preview", text.substring(0, Math.min(12_000, text.length())));
            model.addAttribute("truncated", text.length() > 12_000);
            model.addAttribute("charCount", text.length());
            model.addAttribute("aiConfigured", aiService.isConfigured());
            return "pdf-detail";
        }
    }

    @GetMapping("/ai-exams")
    public String list(Principal principal, Model model) {
        model.addAttribute("exams", aiExams.findByOwner_UsernameOrderByCreatedAtDesc(principal.getName()));
        return "ai-exams";
    }

    @GetMapping("/ai-exams/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        model.addAttribute("exam", ownedExam(id, principal.getName()));
        return "ai-exam-detail";
    }

    @PostMapping("/ai-exams/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        aiExams.delete(ownedExam(id, principal.getName()));
        return "redirect:/ai-exams?deleted";
    }

    private AiExam ownedExam(Long id, String username) {
        return aiExams.findByIdAndOwner_Username(id, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
