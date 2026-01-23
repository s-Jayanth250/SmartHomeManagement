package com.example.shem.service;

import com.example.shem.model.User;
import com.example.shem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // TEMP OTP STORE (DEV ONLY)
    private final Map<String, OtpData> otpStore = new HashMap<>();

    public AuthService(UserRepository userRepository,
                       EmailService emailService,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= REGISTER =================
    public void register(String name, String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);

        userRepository.save(user);
    }

    // ================= SEND OTP =================
    public void sendOtp(String email) {

        userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Email not registered"));

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpStore.put(email, new OtpData(
                otp,
                LocalDateTime.now().plusMinutes(5)
        ));

        emailService.sendOtpEmail(email, otp);
    }

    // ================= VERIFY OTP =================
    public boolean verifyOtp(String email, String otp) {

        OtpData data = otpStore.get(email);

        if (data == null) return false;
        if (data.getExpiry().isBefore(LocalDateTime.now())) return false;
        if (!data.getOtp().equals(otp)) return false;

        return true;
    }

    // ================= RESET PASSWORD =================
    public void resetPassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        otpStore.remove(email);
    }

    // ================= OTP HOLDER =================
    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiry;

        public OtpData(String otp, LocalDateTime expiry) {
            this.otp = otp;
            this.expiry = expiry;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiry() {
            return expiry;
        }
    }
}
