package com.energy.smarthome.service;

import com.energy.smarthome.entity.Device;
import com.energy.smarthome.entity.Schedule;
import com.energy.smarthome.repository.DeviceRepository;
import com.energy.smarthome.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class SchedulerService {

    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private DeviceRepository deviceRepository;
    @Autowired private NotificationService notificationService;

    private final Random random = new Random();

    // 1. Time-Based Automation (Existing Logic)
    public void createSchedule(Long deviceId, String action, LocalDateTime time) {
        Schedule schedule = new Schedule();
        schedule.setDeviceId(deviceId);
        schedule.setAction(action);
        schedule.setScheduledTime(time);
        schedule.setStatus("PENDING");
        scheduleRepository.save(schedule);
    }

    // 2. Temperature Automation (Run every 1 min)
    @Scheduled(fixedRate = 60000)
    public void checkTemperatureRules() {
        // Simulate Room Temp (18°C to 35°C)
        int currentTemp = 18 + random.nextInt(17);

        List<Device> fans = deviceRepository.findAll(); // In real app, filter by type="Fan"

        for (Device device : fans) {
            if ("Fan".equalsIgnoreCase(device.getType()) && "ON".equals(device.getStatus())) {
                if (currentTemp < 24) {
                    device.setStatus("OFF");
                    deviceRepository.save(device);
                    notificationService.sendAutomationAlert(device.getName(), "Turned OFF (Temp < 24°C)");
                }
            }
        }
    }

    // 3. Execute Time-Based Tasks
    @Scheduled(fixedRate = 30000)
    public void executeScheduledTasks() {
        List<Schedule> tasks = scheduleRepository.findByStatus("PENDING");
        LocalDateTime now = LocalDateTime.now();

        for (Schedule task : tasks) {
            if (task.getScheduledTime().isBefore(now)) {
                Device device = deviceRepository.findById(task.getDeviceId()).orElse(null);
                if (device != null) {
                    device.setStatus(task.getAction());
                    deviceRepository.save(device);
                    notificationService.sendAutomationAlert(device.getName(), "Scheduled Action: " + task.getAction());
                }
                task.setStatus("COMPLETED");
                scheduleRepository.save(task);
            }
        }
    }
}