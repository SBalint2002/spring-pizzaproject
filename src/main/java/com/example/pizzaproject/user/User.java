package com.example.pizzaproject.user;

import jakarta.persistence.*;

@Entity
@Table
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String first_name;
    private String last_name;
    private String email;
    private Long order_id;
    private boolean admin;
    private boolean cook;

    //Üres konstruktor
    public User() {

    }

    //Mindent is kérek
    public User(Long id, String first_name, String last_name, String email, Long order_id, boolean admin, boolean cook) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.order_id = order_id;
        this.admin = admin;
        this.cook = cook;
    }

    //Főbb felhasználói adatok (név, email), valszeg bejelentkezéshez majd
    public User(String first_name, String last_name, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
    }

    //Rendelés lekérdezése felhasználó név és email címmel
    public User(String first_name, String last_name, String email, Long order_id){
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.order_id = order_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isCook() {
        return cook;
    }

    public void setCook(boolean cook) {
        this.cook = cook;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", order_id=" + order_id +
                ", admin=" + admin +
                ", cook=" + cook +
                '}';
    }
}
