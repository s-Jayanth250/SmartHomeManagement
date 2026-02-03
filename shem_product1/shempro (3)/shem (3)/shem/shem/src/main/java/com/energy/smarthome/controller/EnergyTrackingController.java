package com.energy.smarthome.controller;

import com.energy.smarthome.entity.UsageLog;
import com.energy.smarthome.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // This means it returns JSON data, not HTML pages
@RequestMapping("/api/energy")
public class EnergyTrackingController {

    @Autowired
    private UsageRepository usageRepository;

    // --- MODULE 3: REAL-TIME DATA API ---
    // The frontend can call this URL to get the raw logs
    @GetMapping("/logs")
    public List<UsageLog> getAllEnergyLogs() {
        // In a real app, you might want to limit this to the last 50 logs
        // e.g., return usageRepository.findTop50ByOrderByTimestampDesc();
        return usageRepository.findAll();
    }
}