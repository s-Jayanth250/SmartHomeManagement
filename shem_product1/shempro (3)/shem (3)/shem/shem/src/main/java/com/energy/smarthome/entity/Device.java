package com.energy.smarthome.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // e.g. "Bedroom AC"
    private String type;        // "AC", "Fan", "Light", "Toy"
    private String status;      // "ON" or "OFF"
    private String location;    // e.g. "1st Floor"

    private Double powerRating; // In Watts
    private Double currentTemperature; // For safety alerts

    // NEW: Tracks which user created this device
    private String addedBy;
}