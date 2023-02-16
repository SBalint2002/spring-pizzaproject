package com.example.pizzaproject.auth;

public class JwtResponse {
    private String JWTToken;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JwtResponse(String status, String JWTToken) {
        this.status = status;
        this.JWTToken = JWTToken;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public void setJWTToken(String JWTToken) {
        this.JWTToken = JWTToken;
    }
}
