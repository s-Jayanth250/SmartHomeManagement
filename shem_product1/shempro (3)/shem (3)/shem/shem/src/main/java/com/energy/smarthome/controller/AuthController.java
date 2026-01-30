package com.energy.smarthome.controller;

import com.energy.smarthome.entity.User;
import com.energy.smarthome.repository.UserRepository;
import com.energy.smarthome.service.AuthService;
import com.energy.smarthome.service.EmailService;
import com.energy.smarthome.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired private AuthService authService;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailService emailService;
    @Autowired private OTPService otpService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/login") public String login() { return "login"; }

    // --- REGISTRATION PAGE WITH "ONE-TIME ADMIN" CHECK ---
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());

        // CHECK: Does an Admin already exist in the database?
        boolean adminExists = userRepository.existsByRole("ROLE_ADMIN");

        // Pass this result to the HTML so we can hide the option
        model.addAttribute("ownerExists", adminExists);

        return "register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam("confirmPassword") String confirmPassword) {
        if (!user.getPassword().equals(confirmPassword)) {
            return "redirect:/register?error=passMismatch";
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "redirect:/register?error=emailExists";
        }

        // DOUBLE CHECK: If someone tries to hack the form to be Admin after one exists
        if (user.getRole().equals("ROLE_ADMIN") && userRepository.existsByRole("ROLE_ADMIN")) {
            return "redirect:/register?error=adminExists";
        }

        authService.registerUser(user);
        emailService.sendEmail(user.getEmail(), "Welcome", "Account Created! Role: " + user.getRole());

        return "redirect:/login?success";
    }

    // --- FORGOT PASSWORD & OTP (Keep existing code) ---
    @GetMapping("/forgot-password") public String showForgotPassword() { return "forgot-password"; }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, Model model) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String otp = otpService.generateOTP(email);
            emailService.sendEmail(email, "Reset Code", "Your OTP is: " + otp);
            model.addAttribute("email", email);
            model.addAttribute("message", "OTP sent to " + email);
            return "verify-otp";
        } else {
            model.addAttribute("error", "Email not registered!");
            return "forgot-password";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestParam("email") String email,
                            @RequestParam("otp") String otp,
                            @RequestParam("newPassword") String newPassword,
                            @RequestParam("confirmPassword") String confirmPassword,
                            Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("email", email);
            model.addAttribute("error", "Passwords do not match!");
            return "verify-otp";
        }
        if (otpService.validateOTP(email, otp)) {
            User user = userRepository.findByEmail(email).get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            otpService.clearOTP(email);
            return "redirect:/login?resetSuccess";
        } else {
            model.addAttribute("email", email);
            model.addAttribute("error", "Invalid OTP!");
            return "verify-otp";
        }
    }

    @GetMapping("/dashboard-redirect")
    public String dashboardRedirect(Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ELECTRICIAN"))) {
            return "redirect:/electrician/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUEST"))) {
            return "redirect:/guest/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CHILD"))) {
            return "redirect:/child/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }
}