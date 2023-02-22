package com.example.pizzaproject.auth;

public class JwtResponse {
    private String JWTToken;
    private String status;
    private String RefreshToken;

    public JwtResponse(String status, String JWTToken, String RefreshToken) {
        this.status = status;
        this.JWTToken = JWTToken;
        this.RefreshToken = RefreshToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public void setJWTToken(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public String getRefreshToken() {
        return RefreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        RefreshToken = refreshToken;
    }
}
