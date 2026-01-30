package com.energy.smarthome.service;

import com.energy.smarthome.entity.User;
import com.energy.smarthome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Search for the user in the database by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 2. Convert the database "User" into a Spring Security "UserDetails" object
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),       // Username
                user.getPassword(),    // Encrypted Password
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())) // Role (e.g., ROLE_ADMIN)
        );
    }
}