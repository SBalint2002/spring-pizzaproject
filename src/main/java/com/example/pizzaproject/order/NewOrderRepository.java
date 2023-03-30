package com.example.pizzaproject.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewOrderRepository extends JpaRepository<NewOrder, Long> {
    Optional<NewOrder> findNewOrderById(Long id);
}
