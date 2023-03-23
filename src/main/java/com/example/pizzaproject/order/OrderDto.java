package com.example.pizzaproject.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String location;
    private String phoneNumber;
    private List<Long> pizzaIds;
}
