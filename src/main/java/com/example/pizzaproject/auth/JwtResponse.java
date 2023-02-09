package com.example.pizzaproject.auth;

public class JwtResponse {
    private String JWTToken;
    public JwtResponse(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public void setJWTToken(String JWTToken) {
        this.JWTToken = JWTToken;
    }
}
