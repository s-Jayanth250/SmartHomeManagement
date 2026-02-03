package com.energy.smarthome.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "usage_logs")
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deviceId; // Reference to the Device ID

    private Double kwh;    // Energy consumed (kilowatt-hours)

    private LocalDateTime timestamp; // When this usage was recorded
}