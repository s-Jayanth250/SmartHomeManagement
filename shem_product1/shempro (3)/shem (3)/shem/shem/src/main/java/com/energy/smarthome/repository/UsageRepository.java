package com.energy.smarthome.repository;

import com.energy.smarthome.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsageRepository extends JpaRepository<UsageLog, Long> {

    // --- MODULE 4: ANALYTICS QUERY ---
    // This custom SQL query calculates the TOTAL energy used by each device.
    // It groups rows by Device ID and sums up the 'kwh' column.
    // Returns a list where each item is an array: [DeviceID, TotalKwh]
    @Query("SELECT u.deviceId, SUM(u.kwh) FROM UsageLog u GROUP BY u.deviceId")
    List<Object[]> getTotalUsagePerDevice();
}