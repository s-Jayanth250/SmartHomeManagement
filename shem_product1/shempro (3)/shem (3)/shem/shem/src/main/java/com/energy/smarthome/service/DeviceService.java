package com.energy.smarthome.service;

import com.energy.smarthome.entity.Device;
import com.energy.smarthome.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    public void addDevice(Device device) {
        // Default status is OFF when added
        device.setStatus("OFF");
        deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        deviceRepository.deleteById(id);
    }

    // Toggles device ON <-> OFF
    public void toggleDevice(Long id) {
        Device device = deviceRepository.findById(id).orElse(null);
        if (device != null) {
            String newStatus = "ON".equals(device.getStatus()) ? "OFF" : "ON";
            device.setStatus(newStatus);
            deviceRepository.save(device);
        }
    }
}