package com.energy.smarthome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired private EmailService emailService;
    private static final String ADMIN_EMAIL = "your_email@gmail.com"; // Replace or fetch from DB

    public void sendHighUsageAlert(String device, double kwh) {
        emailService.sendEmail(ADMIN_EMAIL, "⚠️ High Power Alert: " + device,
                "Warning: " + device + " is consuming abnormal power (" + kwh + " kWh in last 10s). Check for malfunction.");
    }

    public void sendSafetyAlert(String device, String issue) {
        emailService.sendEmail(ADMIN_EMAIL, "🔥 SAFETY ALERT: " + device,
                "URGENT: " + issue + ". Device has been flagged. Please inspect immediately.");
    }

    public void sendAutomationAlert(String device, String action) {
        // Optional: Don't spam email for every auto-action, maybe just log it
        System.out.println("AUTOMATION: " + device + " -> " + action);
    }

    public void sendMonthlyBillEstimate(double totalKwh) {
        double estimatedCost = totalKwh * 0.12; // Example: $0.12 per kWh
        emailService.sendEmail(ADMIN_EMAIL, "💰 Monthly Bill Estimate",
                "Your projected usage is " + totalKwh + " kWh.\nEstimated Bill: $" + String.format("%.2f", estimatedCost));
    }
}