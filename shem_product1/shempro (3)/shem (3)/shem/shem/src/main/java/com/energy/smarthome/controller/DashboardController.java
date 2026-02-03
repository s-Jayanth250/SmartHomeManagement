package com.energy.smarthome.controller;

import com.energy.smarthome.repository.DeviceRepository;
import com.energy.smarthome.repository.UserRepository;
import com.energy.smarthome.service.BillingService; // Import this!
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    @Autowired private DeviceRepository deviceRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private BillingService billingService; // Inject Billing Service

    // --- USER DASHBOARD (Updated) ---
    @GetMapping("/user/dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());

        // Calculate Bill Data
        double currentBill = billingService.calculateCurrentBill();
        double totalUnits = billingService.getTotalUnits();

        // Pass to HTML
        model.addAttribute("currentBill", String.format("%.2f", currentBill));
        model.addAttribute("totalUnits", String.format("%.2f", totalUnits));

        return "dashboard_user";
    }

    // --- OTHER CONTROLLERS (Keep as they were) ---
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "dashboard_admin";
    }

    @GetMapping("/admin/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/electrician/dashboard")
    public String electricianDashboard(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        return "dashboard_electrician";
    }

    @GetMapping("/guest/dashboard")
    public String guestDashboard(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        return "dashboard_guest";
    }

    @GetMapping("/child/dashboard")
    public String childDashboard(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        return "dashboard_child";
    }
}