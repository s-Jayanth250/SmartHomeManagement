package com.energy.smarthome.controller;

import com.energy.smarthome.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AnalyticsController {

    @Autowired private AnalyticsService analyticsService;

    // --- VIEW ANALYTICS PAGE ---
    @GetMapping("/analytics")
    public String showAnalyticsPage() {
        return "analytics";
    }

    // --- API FOR CHART.JS (JSON DATA) ---
    @GetMapping("/api/analytics/usage")
    @ResponseBody
    public List<Object[]> getUsageStats() {
        // Returns [DeviceID, TotalKwh] for graphs
        return analyticsService.generateUsageReport();
    }
}