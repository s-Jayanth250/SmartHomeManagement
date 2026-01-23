package com.example.shem.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@shem.local");   // ✅ REQUIRED
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP - SHEM");
        message.setText(
                "Your OTP is: " + otp +
                        "\n\nThis OTP is valid for 5 minutes.\n\nSHEM Team"
        );

        mailSender.send(message);
    }
}
