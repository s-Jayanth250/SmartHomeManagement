package com.energy.smarthome.repository;

import com.energy.smarthome.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Used for Login: Find user by email
    Optional<User> findByEmail(String email);

    // Used for Registration: Check if an Owner/Admin already exists
    // (We use this to hide the "Owner" option in the dropdown)
    boolean existsByRole(String role);
}