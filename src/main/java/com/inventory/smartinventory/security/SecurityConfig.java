package com.inventory.smartinventory.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Central Spring Security configuration.
 *
 * Key decisions:
 *   - Stateless sessions (no cookies) — JWT handles auth
 *   - CORS enabled for localhost:3000 (React frontend)
 *   - /api/auth/** endpoints are PUBLIC (login + register)
 *   - DELETE endpoints require ADMIN role
 *   - POST /api/auth/register requires ADMIN role (only admins create users)
 *   - All other /api/** endpoints require authentication (any role)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // ============================================
    // SECURITY FILTER CHAIN
    // Defines which endpoints are public vs protected
    // ============================================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF — not needed for stateless JWT APIs
            .csrf(csrf -> csrf.disable())

            // Enable CORS with our configuration below
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Stateless sessions — no server-side session, JWT only
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Endpoint access rules
            .authorizeHttpRequests(auth -> auth

                // PUBLIC — Frontend static files & login endpoint (no token needed)
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/dashboard.html",
                    "/css/**",
                    "/js/**",
                    "/pages/**",
                    "/favicon.ico",
                    "/api/auth/login"
                ).permitAll()

                // ADMIN ONLY — register new users
                .requestMatchers("/api/auth/register").hasRole("ADMIN")

                // ADMIN ONLY — all DELETE operations
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                // AUTHENTICATED — everything else requires a valid JWT
                .anyRequest().authenticated()
            )

            // Add our JWT filter BEFORE Spring's default username/password filter
            .addFilterBefore(jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ============================================
    // PASSWORD ENCODER
    // BCrypt — industry standard for hashing passwords
    // ============================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ============================================
    // AUTHENTICATION MANAGER
    // Used by AuthController to authenticate login credentials
    // ============================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ============================================
    // CORS CONFIGURATION
    // Allows the React frontend (localhost:3000) to call our API
    // ============================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // Allow all origins for dev
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

