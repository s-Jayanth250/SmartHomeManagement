package com.energy.smarthome.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long deviceId;          // Which device to control

    private String action;          // "ON" or "OFF"

    private LocalDateTime scheduledTime; // When to execute

    private String status;          // "PENDING" or "COMPLETED"
}