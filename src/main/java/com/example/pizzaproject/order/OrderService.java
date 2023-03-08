package com.example.pizzaproject.order;

import com.example.pizzaproject.pizza.Pizza;
import com.example.pizzaproject.pizza.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public void addNewOrder(Order order){
        orderRepository.save(order);
    }

    public Optional<Order> findOrderByName(int id) {
        Optional<Order> order = orderRepository.findOrderByName(id);
        if (order.isPresent()) {
            return order;
        }
        return Optional.empty();
    }

    @Transactional
    public void updateOrder(Long orderId, Order updateOrder) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Order with id " + orderId + " does not exist"));

        if (updateOrder.getUser_id() != null && updateOrder.getUser_id() > 0) {
            order.setUser_id(updateOrder.getUser_id());
        }

        if (updateOrder.getPizza_id() != null && updateOrder.getPizza_id() > 0) {
            order.setPizza_id(updateOrder.getPizza_id());
        }

        if(updateOrder.getLocation() != null && updateOrder.getLocation().length() > 0) {
            order.setLocation(updateOrder.getLocation());
        }

        if(updateOrder.getOrder_date() != null) {
            order.setOrder_date(updateOrder.getOrder_date());
        }

        if (updateOrder.getPrice() > 0){
            order.setPrice(updateOrder.getPrice());
        }

        if (updateOrder.getPhone_number() > 0 && String.valueOf(updateOrder.getPhone_number()).length() < 16) {
            order.setPhone_number(updateOrder.getPhone_number());
        }
        if (updateOrder.isReady()) {
            order.setReady(true);
        }
        if (!updateOrder.isReady()) {
            order.setReady(false);
        }
    }
}
