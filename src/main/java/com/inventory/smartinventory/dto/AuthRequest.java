package com.inventory.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for login requests.
 * POST /api/auth/login body: { "username": "admin", "password": "secret" }
 */
public class AuthRequest {

    @NotBlank @Size(max = 50)
    private String username;
    @NotBlank @Size(min = 8, max = 128)
    private String password;

    public AuthRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
