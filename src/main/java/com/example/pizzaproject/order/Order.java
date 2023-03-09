package com.example.pizzaproject.order;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "pizza_id")
    private Long pizza_id;

    @Column(name = "location")
    private String location;

    @Column(name = "order_date")
    private Date order_date;

    @Column(name = "price")
    private int price;

    @Column(name = "phone_number")
    private int phone_number;

    @Column(name = "ready")
    private boolean ready;

    public Order() {
    }

    public Order(Long user_id, Long pizza_id, String location, Date order_date, int price, int phone_number, boolean ready) {
        this.user_id = user_id;
        this.pizza_id = pizza_id;
        this.location = location;
        this.order_date = order_date;
        this.price = price;
        this.phone_number = phone_number;
        this.ready = ready;
    }

    public Order(Long id, Long user_id, Long pizza_id, String location, Date order_date, int price, int phone_number, boolean ready) {
        this.id = id;
        this.user_id = user_id;
        this.pizza_id = pizza_id;
        this.location = location;
        this.order_date = order_date;
        this.price = price;
        this.phone_number = phone_number;
        this.ready = ready;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getPizza_id() {
        return pizza_id;
    }

    public void setPizza_id(Long pizza_id) {
        this.pizza_id = pizza_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", pizza_id=" + pizza_id +
                ", location='" + location + '\'' +
                ", order_date=" + order_date +
                ", price=" + price +
                ", phone_number=" + phone_number +
                ", ready=" + ready +
                '}';
    }
}
