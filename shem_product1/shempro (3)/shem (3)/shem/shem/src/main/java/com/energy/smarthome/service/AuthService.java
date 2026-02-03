package com.energy.smarthome.service;

import com.energy.smarthome.entity.User;
import com.energy.smarthome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user with encrypted password
    public void registerUser(User user) {
        // 1. Encrypt the plain text password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 2. Set default active status if needed
        // user.setActive(true);

        // 3. Save to DB
        userRepository.save(user);
    }
}