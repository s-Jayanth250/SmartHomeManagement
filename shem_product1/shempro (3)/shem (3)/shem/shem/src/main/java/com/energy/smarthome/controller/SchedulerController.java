package com.energy.smarthome.controller;

import com.energy.smarthome.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin") // Restricted to Admin
public class SchedulerController {

    @Autowired private SchedulerService schedulerService;

    // --- SET SCHEDULE ---
    @PostMapping("/schedule-device")
    public String scheduleDevice(
            @RequestParam Long deviceId,
            @RequestParam String action, // "ON" or "OFF"
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime) {

        schedulerService.createSchedule(deviceId, action, scheduledTime);
        return "redirect:/admin/dashboard?scheduled";
    }
}