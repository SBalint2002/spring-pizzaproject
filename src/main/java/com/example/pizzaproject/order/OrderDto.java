package com.example.pizzaproject.order;

import java.util.List;

public class OrderDto {
    private String location;
    private String phoneNumber;
    private List<Long> pizzaIds;

    public OrderDto(String location, String phoneNumber, List<Long> pizzaIds) {
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.pizzaIds = pizzaIds;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Long> getPizzaIds() {
        return pizzaIds;
    }

    public void setPizzaIds(List<Long> pizzaIds) {
        this.pizzaIds = pizzaIds;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
