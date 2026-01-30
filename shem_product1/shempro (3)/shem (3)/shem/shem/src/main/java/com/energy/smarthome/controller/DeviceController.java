package com.energy.smarthome.controller;

import com.energy.smarthome.entity.Device;
import com.energy.smarthome.service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeviceController {

    @Autowired private DeviceService deviceService;

    // --- UNIVERSAL ADD DEVICE (For Admin, Guest, Child, Electrician) ---
    @PostMapping("/device/add")
    public String addDevice(@ModelAttribute Device device, Authentication auth) {

        // 1. Tag the device with the logged-in user's email/username
        if (auth != null) {
            device.setAddedBy(auth.getName());
        } else {
            device.setAddedBy("Unknown");
        }

        // 2. Set default status if missing
        if (device.getStatus() == null) {
            device.setStatus("OFF");
        }

        // 3. Save Device
        deviceService.addDevice(device);

        // 4. Smart Redirect (Send them back to THEIR specific dashboard)
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ELECTRICIAN"))) {
            return "redirect:/electrician/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_GUEST"))) {
            return "redirect:/guest/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CHILD"))) {
            return "redirect:/child/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }

    // --- DELETE DEVICE (Admin Only) ---
    @GetMapping("/admin/delete-device/{id}")
    public String deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return "redirect:/admin/dashboard";
    }

    // --- UNIVERSAL TOGGLE (For Everyone) ---
    @GetMapping({"/device/toggle/{id}"})
    public String toggleDevice(@PathVariable Long id, HttpServletRequest request) {
        deviceService.toggleDevice(id);

        // Returns user to the previous page they were on (Admin, Guest, or Child page)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/user/dashboard");
    }
}