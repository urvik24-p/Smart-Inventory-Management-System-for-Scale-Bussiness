package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.dto.AuthRequest;
import com.inventory.smartinventory.dto.AuthResponse;
import com.inventory.smartinventory.dto.RegisterRequest;
import com.inventory.smartinventory.entity.AppUser;
import com.inventory.smartinventory.entity.UserRole;
import com.inventory.smartinventory.exception.InvalidOperationException;
import com.inventory.smartinventory.repository.UserRepository;
import com.inventory.smartinventory.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * REST controller for authentication.
 *
 * Endpoints:
 *   POST /api/auth/login    ? authenticate and receive a JWT token (PUBLIC)
 *   POST /api/auth/register ? create a new user account (ADMIN ONLY)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ============================================
    // LOGIN
    // POST /api/auth/login
    // Body: { "username": "admin", "password": "secret" }
    // Returns: { "token": "eyJ...", "username": "admin", "role": "ADMIN" }
    // ============================================

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {

        // Let Spring Security verify username + password against the DB
        // Throws BadCredentialsException if wrong ? caught by GlobalExceptionHandler (500)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If we reach here, credentials are valid - generate a JWT
        String token = jwtUtil.generateToken(request.getUsername());

        // Look up the user's role to include in the response
        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        return ResponseEntity.ok(
                new AuthResponse(token, user.getUsername(), user.getRole().name())
        );
    }

    // ============================================
    // REGISTER
    // POST /api/auth/register
    // Body: { "username": "staff1", "password": "pass", "role": "STAFF" }
    // Only accessible by ADMIN (enforced in SecurityConfig)
    // ============================================

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {

        // Check if username already taken
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new InvalidOperationException(
                    "Username already taken: " + request.getUsername());
        }

        // Parse and validate the role
        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOperationException(
                    "Invalid role: '" + request.getRole() + "'. Use ADMIN or STAFF.");
        }

        // Create and save the user with a hashed password
        AppUser newUser = new AppUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(role);

        userRepository.save(newUser);

        return ResponseEntity.status(201).body("User registered successfully: " + request.getUsername());
    }
}
