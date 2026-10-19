package com.inventory.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO for registering a new user (ADMIN only endpoint).
 * POST /api/auth/register body: { "username": "staff1", "password": "pass", "role": "STAFF" }
 */
public class RegisterRequest {

    @NotBlank @Pattern(regexp = "^[A-Za-z0-9._-]{3,50}$")
    private String username;
    @NotBlank @Size(min = 12, max = 128)
    private String password;
    @NotBlank @Pattern(regexp = "(?i)ADMIN|STAFF")
    private String role;

    public RegisterRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
