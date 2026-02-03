package com.energy.smarthome.service;

import com.energy.smarthome.entity.Device;
import com.energy.smarthome.entity.UsageLog;
import com.energy.smarthome.repository.DeviceRepository;
import com.energy.smarthome.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class EnergyTrackingService {

    @Autowired private DeviceRepository deviceRepository;
    @Autowired private UsageRepository usageRepository;
    @Autowired private NotificationService notificationService;

    private final Random random = new Random();

    // --- THE HEARTBEAT: Runs every 10 seconds ---
    @Scheduled(fixedRate = 10000)
    public void simulateSmartHome() {
        List<Device> devices = deviceRepository.findAll();

        for (Device device : devices) {
            // Only calculate energy if the device is ON
            if ("ON".equalsIgnoreCase(device.getStatus())) {

                // 1. Get Power Rating (Default to 100W if missing)
                double watts = (device.getPowerRating() != null) ? device.getPowerRating() : 100.0;

                // 2. Calculate Base Consumption for 10 seconds
                // Formula: (Watts / 1000) * (10 seconds / 3600 seconds per hour)
                double kWh = (watts / 1000.0) * (10.0 / 3600.0);

                // 3. --- AUTOMATIC FLUCTUATION MAGIC ---
                // Real devices don't use constant power. Voltage varies.
                // This line randomly changes usage by +/- 10% to look realistic.
                // "0.9" is the minimum (90%), "+ (0.2...)" adds up to 20% variance.
                kWh = kWh * (0.9 + (0.2 * random.nextDouble()));

                // 4. Save to Database (This updates the Charts)
                UsageLog log = new UsageLog();
                log.setDeviceId(device.getId());
                log.setTimestamp(LocalDateTime.now());
                log.setKwh(kWh);
                usageRepository.save(log);

                // 5. Check for Safety Alerts

                // Alert A: High Usage Spike (Abnormal Power Draw)
                if (kWh > 0.05) {
                    notificationService.sendHighUsageAlert(device.getName(), kWh);
                }

                // Alert B: Overheating Simulation (1% Random Chance)
                // In a real app, this would come from a sensor.
                if (random.nextInt(100) > 98) {
                    notificationService.sendSafetyAlert(device.getName(), "OVERHEATING DETECTED! (> 80°C)");
                }
            }
        }
    }
}