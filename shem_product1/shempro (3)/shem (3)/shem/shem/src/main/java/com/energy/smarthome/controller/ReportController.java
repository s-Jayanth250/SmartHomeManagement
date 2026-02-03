package com.energy.smarthome.controller;

import com.energy.smarthome.entity.UsageLog;
import com.energy.smarthome.repository.UsageRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class ReportController {

    @Autowired private UsageRepository usageRepository;

    @GetMapping("/admin/export-usage")
    public void exportCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"energy_report.csv\"");

        List<UsageLog> logs = usageRepository.findAll();

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Log ID,Device ID,kWh,Timestamp"); // Header
            for (UsageLog log : logs) {
                writer.println(log.getId() + "," + log.getDeviceId() + "," + log.getKwh() + "," + log.getTimestamp());
            }
        }
    }
}