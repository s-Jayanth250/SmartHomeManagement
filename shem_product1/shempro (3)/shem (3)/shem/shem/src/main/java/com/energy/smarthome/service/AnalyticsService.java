package com.energy.smarthome.service;

import com.energy.smarthome.repository.UsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyticsService {

    @Autowired
    private UsageRepository usageRepository;

    public List<Object[]> generateUsageReport() {
        // Calls the custom query in Repository to get Total Usage per Device
        return usageRepository.getTotalUsagePerDevice();
    }
}