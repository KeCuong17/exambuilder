package com.example.exambuilder.web;

import com.example.exambuilder.model.UploadedDocument;
import com.example.exambuilder.repository.UploadedDocumentRepository;
import com.example.exambuilder.repository.UserAccountRepository;
import com.example.exambuilder.service.PdfTextService;
import com.example.exambuilder.service.AiExamService;
import com.example.exambuilder.service.PdfTextService.PdfExtractResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping("/documents")
public class DocumentController {
    private final PdfTextService pdfTextService;
    private final UploadedDocumentRepository documents;
    private final UserAccountRepository users;
    private final AiExamService aiExamService;

    public DocumentController(PdfTextService pdfTextService,
                              UploadedDocumentRepository documents,
                              UserAccountRepository users,
                              AiExamService aiExamService) {
        this.pdfTextService = pdfTextService;
        this.documents = documents;
        this.users = users;
        this.aiExamService = aiExamService;
    }

    @GetMapping
    public String uploadPage(Principal principal, Model model) {
        model.addAttribute("documents", documents.findByOwner_UsernameOrderByCreatedAtDesc(principal.getName()));
        return "pdf-upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("pdf") MultipartFile pdf,
                         @RequestParam(defaultValue = "Chương 2: Hàm số") String chapter,
                         @RequestParam(defaultValue = "35") int startPage,
                         @RequestParam(defaultValue = "60") int endPage,
                         @RequestParam(defaultValue = "20") int questionCount,
                         @RequestParam(defaultValue = "MIXED") String difficulty,
                         @RequestParam(defaultValue = "MIXED") String questionType,
                         Principal principal, Model model) {
        try {
            if (chapter == null || chapter.isBlank() || chapter.length() > 120) {
                throw new IllegalArgumentException("Tên chương phải có từ 1 đến 120 ký tự.");
            }
            if (questionCount < 1 || questionCount > 20) {
                throw new IllegalArgumentException("Số câu hỏi phải từ 1 đến 20.");
            }
            if (!java.util.Set.of("EASY", "MEDIUM", "HARD", "MIXED").contains(difficulty)) {
                throw new IllegalArgumentException("Độ khó không hợp lệ.");
            }
            if (!java.util.Set.of("MULTIPLE_CHOICE", "ESSAY", "MIXED").contains(questionType)) {
                throw new IllegalArgumentException("Dạng câu hỏi không hợp lệ.");
            }
            PdfExtractResult extracted = pdfTextService.extract(pdf, startPage, endPage);
            UploadedDocument doc = new UploadedDocument();
            doc.setOwner(users.findByUsername(principal.getName())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)));
            String originalName = pdf.getOriginalFilename().replace('\\', '/');
            originalName = originalName.substring(originalName.lastIndexOf('/') + 1);
            doc.setFilename(originalName.substring(0, Math.min(originalName.length(), 200)));
            doc.setChapter(chapter.trim());
            doc.setStartPage(startPage);
            doc.setEndPage(endPage);
            doc.setTotalPages(extracted.totalPages());
            doc.setQuestionCount(questionCount);
            doc.setDifficulty(difficulty);
            doc.setQuestionType(questionType);
            doc.setExtractedText(extracted.text());
            documents.save(doc);
            return "redirect:/documents/" + doc.getId();
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("documents", documents.findByOwner_UsernameOrderByCreatedAtDesc(principal.getName()));
            model.addAttribute("chapterValue", chapter);
            model.addAttribute("startPageValue", startPage);
            model.addAttribute("endPageValue", endPage);
            model.addAttribute("questionCountValue", questionCount);
            model.addAttribute("difficultyValue", difficulty);
            model.addAttribute("questionTypeValue", questionType);
            return "pdf-upload";
        }
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        UploadedDocument document = documents.findByIdAndOwner_Username(id, principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String extractedText = document.getExtractedText();
        model.addAttribute("document", document);
        model.addAttribute("preview", extractedText.substring(0, Math.min(12_000, extractedText.length())));
        model.addAttribute("truncated", extractedText.length() > 12_000);
        model.addAttribute("charCount", extractedText.length());
        model.addAttribute("aiConfigured", aiExamService.isConfigured());
        return "pdf-detail";
    }
}
