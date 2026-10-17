package com.inventory.smartinventory.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Maps to the "users" table.
 *
 * Stores login credentials and role for each system user.
 * Passwords are stored as BCrypt hashes - never plain text.
 */
@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    // Unique login name
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    // BCrypt-hashed password - never store plain text
    @Column(name = "password", nullable = false)
    private String password;

    // ADMIN or STAFF
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 10)
    private UserRole role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public AppUser() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
