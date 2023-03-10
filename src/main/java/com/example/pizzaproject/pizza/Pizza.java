package com.example.pizzaproject.pizza;

import com.example.pizzaproject.order.OrderPizza;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="PIZZAS")
public class Pizza {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "description")
    private String description;
    @Column(name = "picture")
    private String picture;

    @JsonIgnore
    @OneToMany(mappedBy = "pizza")
    private List<OrderPizza> orderPizzas;

    public List<OrderPizza> getOrderPizzas() {
        return orderPizzas;
    }

    public void setOrderPizzas(List<OrderPizza> orderPizzas) {
        this.orderPizzas = orderPizzas;
    }

    public Pizza() {

    }

    public Pizza(String name, int price, String description, String picture) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.picture = picture;
    }

    public Pizza(Long id, String name, int price, String description, String picture) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.picture = picture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", orderPizzas=" + orderPizzas +
                '}';
    }
}
