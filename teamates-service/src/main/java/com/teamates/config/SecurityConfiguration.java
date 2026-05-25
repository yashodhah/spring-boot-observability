package com.teamates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

// FLAW: permits all requests — no authentication or authorization
// FLAW: disables CSRF for all endpoints, not just stateless APIs
// FLAW: CORS not configured – open to all origins
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // FLAW: CSRF disabled globally
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // FLAW: all endpoints open without authentication
            );
        return http.build();
    }
}
