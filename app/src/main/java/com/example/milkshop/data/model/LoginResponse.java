package com.example.milkshop.data.model;

public class LoginResponse {
    private String token;
    private String refreshToken;
    private String role;
    private String message;

    public LoginResponse(String token, String refreshToken, String role, String message) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
        this.message = message;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
