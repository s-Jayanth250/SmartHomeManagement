package com.energy.smarthome.config;

import com.energy.smarthome.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Password Encoder (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. User Details Service (Connects to DB)
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    // 3. Authentication Provider (Links DB user lookup with Password Encoder)
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 4. Security Filter Chain (The Rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // A. PUBLIC ACCESS (No Login Required)
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/saveUser",
                                "/forgot-password",
                                "/send-otp",
                                "/verify-otp",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        // B. ROLE-BASED ACCESS CONTROL

                        // Owner/Admin Only
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Electrician & Admin
                        .requestMatchers("/electrician/**").hasAnyRole("ADMIN", "ELECTRICIAN")

                        // Family Members & Staff (Everyone else)
                        .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER", "CHILD", "GUEST", "STAFF")

                        // Any other link requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")               // Custom Login HTML
                        .loginProcessingUrl("/login")      // Form POST action
                        .defaultSuccessUrl("/dashboard-redirect", true) // Redirects to AuthController to decide dashboard
                        .failureUrl("/login?error")        // Failed login redirect
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider()) // Set the DB auth provider
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity in development

        return http.build();
    }
}