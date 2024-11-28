package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder for security
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disabling CSRF for stateless authentication
                .csrf(csrf -> csrf.disable())

                // Defining the permissions for different roles
                .authorizeHttpRequests(authz -> authz
                        // Allowing access to the login and swagger endpoints
                        .requestMatchers("/api/auth/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // Host role can insert listings
                        .requestMatchers("/api/v1/listings").hasRole("HOST")

                        // Guest role can query listings, book a stay, and review a stay
                        .requestMatchers("/api/v1/listings/query").hasRole("GUEST")
                        .requestMatchers("/api/v1/listings/book").hasRole("GUEST")
                        .requestMatchers("/api/v1/listings/review").hasRole("GUEST")

                        // Admin role can report listings
                        .requestMatchers("/api/admin/report").hasRole("ADMIN")

                        // Restrict other URLs to authenticated users only
                        .anyRequest().authenticated()
                )

                // Adding JWT filter for token validation
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // Authentication manager for JWT
    }
}
