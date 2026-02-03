package com.energy.smarthome.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {

    // Simple storage: Email -> OTP
    // (In a real app, use a Database or Redis with expiration)
    private Map<String, String> otpStorage = new HashMap<>();

    public String generateOTP(String email) {
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000); // Generate 6-digit number
        String otp = String.valueOf(otpValue);
        otpStorage.put(email, otp);
        return otp;
    }

    public boolean validateOTP(String email, String otpEntered) {
        if (otpStorage.containsKey(email)) {
            String correctOtp = otpStorage.get(email);
            return correctOtp.equals(otpEntered);
        }
        return false;
    }

    public void clearOTP(String email) {
        otpStorage.remove(email);
    }
}