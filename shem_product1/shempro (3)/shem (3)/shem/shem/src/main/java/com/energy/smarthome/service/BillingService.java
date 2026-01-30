package com.energy.smarthome.service;

import com.energy.smarthome.entity.UsageLog;
import com.energy.smarthome.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingService {

    @Autowired
    private UsageRepository usageRepository;

    // INDIA ELECTRICITY RATE (Average ₹8 per Unit/kWh)
    private static final double RATE_PER_UNIT = 8.00;

    public double calculateCurrentBill() {
        List<UsageLog> allLogs = usageRepository.findAll();

        // Sum up all kWh recorded
        double totalKwh = 0;
        for (UsageLog log : allLogs) {
            totalKwh += log.getKwh();
        }

        // Calculate Cost: Total Units * Rate
        return totalKwh * RATE_PER_UNIT;
    }

    public double getTotalUnits() {
        List<UsageLog> allLogs = usageRepository.findAll();
        double totalKwh = 0;
        for (UsageLog log : allLogs) {
            totalKwh += log.getKwh();
        }
        return totalKwh;
    }
}