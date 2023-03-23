package com.example.pizzaproject.order;

import com.example.pizzaproject.pizza.Pizza;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "location")
    private String location;

    @Column(name = "order_date")
    private Date order_date;

    @Column(name = "price")
    private int price;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "ready")
    private boolean ready;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderPizza> orderPizzas;

    public Order(Long user_id, String location, Date order_date, int price, String phone_number, boolean ready){
        this.user_id = user_id;
        this.location = location;
        this.order_date = order_date;
        this.price = price;
        this.phone_number = phone_number;
        this.ready = ready;
        this.orderPizzas = new ArrayList<>();
    }

    public void addPizza(Pizza pizza) {
        orderPizzas.add(new OrderPizza(this, pizza));
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", location='" + location + '\'' +
                ", order_date=" + order_date +
                ", price=" + price +
                ", phone_number='" + phone_number + '\'' +
                ", ready=" + ready +
                ", orderPizzas=" + orderPizzas +
                '}';
    }
}