package com.energy.smarthome.repository;

import com.energy.smarthome.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    // Standard CRUD operations are built-in.
    // You can add methods like findByLocation(String location) here if needed later.
}