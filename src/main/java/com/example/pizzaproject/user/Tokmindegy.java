package com.example.pizzaproject.user;

public class Tokmindegy {
    private String JWTToken;

    public Tokmindegy(String JWTToken) {
        this.JWTToken = JWTToken;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public void setJWTToken(String JWTToken) {
        this.JWTToken = JWTToken;
    }
}
