package com.example.pizzaproject.pizza;

import com.example.pizzaproject.pizza.Pizza;
import com.example.pizzaproject.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    @Query("SELECT u FROM Pizza u WHERE u.name = ?1")
    Optional<Pizza> findPizzaByName(String name);
}