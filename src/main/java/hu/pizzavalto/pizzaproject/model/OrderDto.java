package hu.pizzavalto.pizzaproject.model;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private String location;
    private Date orderDate;
    private int price;
    private String phoneNumber;
    private boolean ready;

    public OrderDto(Order order) {
        this.id = order.getId();
        this.userId = order.getUser().getId();
        this.location = order.getLocation();
        this.orderDate = order.getOrder_date();
        this.price = order.getPrice();
        this.phoneNumber = order.getPhone_number();
        this.ready = order.isReady();
    }
}