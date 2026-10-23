package com.example.exambuilder.web;

import com.example.exambuilder.model.Exam;
import com.example.exambuilder.service.ExamService;
import com.example.exambuilder.web.dto.ExamForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@Controller
@RequestMapping("/exams")
public class ExamController {
    private final ExamService service;

    public ExamController(ExamService service) { this.service = service; }

    @GetMapping
    public String index(Principal principal, Model model) {
        model.addAttribute("exams", service.listMine(principal.getName()));
        return "exams";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        formModel(model, ExamForm.blank(), null);
        return "form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ExamForm form,
                         BindingResult validation, Principal principal, Model model) {
        if (validation.hasErrors()) {
            formModel(model, form, null);
            model.addAttribute("error", "Hãy nhập đủ thông tin cho mỗi câu hỏi (tối đa 30 câu).");
            return "form";
        }
        Exam exam = service.create(principal.getName(), form);
        return "redirect:/exams/" + exam.getId() + "?saved";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        model.addAttribute("exam", service.getMine(id, principal.getName()));
        return "detail";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Principal principal, Model model) {
        Exam exam = service.getMine(id, principal.getName());
        formModel(model, ExamForm.from(exam), id);
        return "form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") ExamForm form,
                         BindingResult validation, Principal principal, Model model) {
        // Check ownership even when the submitted form is invalid.
        service.getMine(id, principal.getName());
        if (validation.hasErrors()) {
            formModel(model, form, id);
            model.addAttribute("error", "Hãy kiểm tra lại thông tin đề thi và các đáp án.");
            return "form";
        }
        service.update(id, principal.getName(), form);
        return "redirect:/exams/" + id + "?saved";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        service.delete(id, principal.getName());
        return "redirect:/exams?deleted";
    }

    private void formModel(Model model, ExamForm form, Long id) {
        model.addAttribute("form", form);
        model.addAttribute("examId", id);
        model.addAttribute("pageTitle", id == null ? "Tạo đề thi mới" : "Chỉnh sửa đề thi");
        model.addAttribute("postUrl", id == null ? "/exams" : "/exams/" + id);
    }
}
