package com.example.exambuilder.web;

import com.example.exambuilder.model.UserAccount;
import com.example.exambuilder.repository.UserAccountRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Locale;

@Controller
public class AuthController {
    private final UserAccountRepository users;
    private final PasswordEncoder encoder;

    public AuthController(UserAccountRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String registerPage() { return "register"; }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        String normalized = username.trim().toLowerCase(Locale.ROOT);
        model.addAttribute("username", normalized);
        if (!normalized.matches("[a-z0-9_]{3,30}")) {
            model.addAttribute("error", "Tên đăng nhập phải có 3–30 ký tự: a-z, 0-9 hoặc dấu _.");
            return "register";
        }
        if (password.length() < 8 || password.length() > 72) {
            model.addAttribute("error", "Mật khẩu phải từ 8 đến 72 ký tự.");
            return "register";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Hai mật khẩu không khớp.");
            return "register";
        }
        if (users.existsByUsername(normalized)) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng.");
            return "register";
        }
        UserAccount user = new UserAccount();
        user.setUsername(normalized);
        user.setPasswordHash(encoder.encode(password));
        try {
            users.saveAndFlush(user);
        } catch (DataIntegrityViolationException duplicate) {
            model.addAttribute("error", "Tên đăng nhập đã được sử dụng.");
            return "register";
        }
        return "redirect:/login?registered";
    }
}
