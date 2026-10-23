package com.example.exambuilder.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class PdfTextService {
    private static final long MAX_FILE_BYTES = 30L * 1024 * 1024;
    private static final int MAX_SELECTED_PAGES = 30;
    private static final int MAX_EXTRACTED_CHARS = 200_000;

    public PdfExtractResult extract(MultipartFile file, int startPage, int endPage) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Bạn chưa chọn file PDF.");
        }
        if (file.getSize() > MAX_FILE_BYTES) {
            throw new IllegalArgumentException("File PDF phải có dung lượng tối đa 15 MB.");
        }
        String name = file.getOriginalFilename();
        if (name == null || !name.toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            throw new IllegalArgumentException("Vui lòng chọn đúng file .pdf.");
        }
        if (startPage < 1 || endPage < startPage || endPage - startPage + 1 > MAX_SELECTED_PAGES) {
            throw new IllegalArgumentException("Chọn tối đa 30 trang liên tiếp, bắt đầu từ trang 1.");
        }
        try {
            byte[] bytes = file.getBytes();
            // Reject obvious non-PDF files even when they are named .pdf.
            if (bytes.length < 5 || !new String(bytes, 0, 5, StandardCharsets.US_ASCII).equals("%PDF-")) {
                throw new IllegalArgumentException("File tải lên không phải tài liệu PDF hợp lệ.");
            }
            try (PDDocument document = Loader.loadPDF(bytes)) {
                if (document.isEncrypted()) {
                    throw new IllegalArgumentException("File PDF có mật khẩu chưa được hỗ trợ.");
                }
                int totalPages = document.getNumberOfPages();
                if (endPage > totalPages) {
                    throw new IllegalArgumentException("PDF chỉ có " + totalPages + " trang. Hãy chọn lại khoảng trang.");
                }
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setSortByPosition(true);
                StringBuilder content = new StringBuilder();
                for (int page = startPage; page <= endPage; page++) {
                    stripper.setStartPage(page);
                    stripper.setEndPage(page);
                    String pageText = stripper.getText(document).trim();
                    if (!pageText.isBlank()) {
                        content.append("\n\n[PDF_PAGE ").append(page).append("]\n")
                            .append(pageText).append('\n');
                    }
                }
                String text = content.toString().trim();
                if (text.isBlank()) {
                    throw new IllegalArgumentException("Không lấy được chữ từ khoảng trang này. PDF scan ảnh cần OCR (sẽ làm ở bước sau).");
                }
                if (text.length() > MAX_EXTRACTED_CHARS) {
                    throw new IllegalArgumentException("Đoạn trích quá dài. Hãy chọn ít trang hơn.");
                }
                return new PdfExtractResult(totalPages, text);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Không thể đọc PDF. File có thể bị hỏng hoặc được bảo vệ bằng mật khẩu.", e);
        }
    }

    public record PdfExtractResult(int totalPages, String text) {}
}
