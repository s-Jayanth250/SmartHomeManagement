package com.energy.smarthome.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role; // ROLE_ADMIN, ROLE_ELECTRICIAN, ROLE_USER, etc.

    // Optional: For OTP functionality
    private String otp;
    private LocalDateTime otpExpiry;
}