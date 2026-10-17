package com.inventory.smartinventory.dto;

/**
 * DTO for login requests.
 * POST /api/auth/login body: { "username": "admin", "password": "secret" }
 */
public class AuthRequest {

    private String username;
    private String password;

    public AuthRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
