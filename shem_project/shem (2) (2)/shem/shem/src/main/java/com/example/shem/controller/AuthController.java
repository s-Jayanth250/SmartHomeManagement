package com.example.shem.controller;

import com.example.shem.model.User;
import com.example.shem.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ================= REGISTER =================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password
    ) {
        authService.register(name, email, password);
        return "redirect:/login";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    // ================= FORGOT PASSWORD =================
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String sendOtp(@RequestParam String email) {
        authService.sendOtp(email);
        return "redirect:/verify-otp?email=" + email;
    }

    // ================= VERIFY OTP =================
    @GetMapping("/verify-otp")
    public String verifyOtpPage(
            @RequestParam String email,
            Model model
    ) {
        model.addAttribute("email", email);
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        boolean verified = authService.verifyOtp(email, otp);

        if (!verified) {
            return "redirect:/verify-otp?email=" + email + "&error=true";
        }

        // ✅ THIS REDIRECT NOW WORKS
        return "redirect:/reset-password?email=" + email;
    }

    // ================= RESET PASSWORD =================
    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam String email,
            Model model
    ) {
        model.addAttribute("email", email);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String password
    ) {
        authService.resetPassword(email, password);
        return "redirect:/login?resetSuccess=true";
    }
}
