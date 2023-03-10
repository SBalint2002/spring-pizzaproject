package com.example.pizzaproject.order;

import com.example.pizzaproject.pizza.Pizza;
import com.example.pizzaproject.pizza.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/order")
public class OrderController {

    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/get-all")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }


    @PostMapping(path = "/add-order")
    public ResponseEntity<String> addNewOrder(@RequestBody OrderDto orderDto) {
        try {
            int price = orderService.sumPrice(orderDto.getPizzaIds());
            Order order = new Order(Long.valueOf(1), // user id :DDDDD
                    orderDto.getLocation(),
                    new Date(),
                    price,
                    orderDto.getPhoneNumber(),
                    false);
            for (Long pizzaId : orderDto.getPizzaIds()) {
                Pizza pizza = pizzaRepository.findById(pizzaId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza not found"));
                order.addPizza(pizza);
            }
            orderService.addNewOrder(order);
            return new ResponseEntity<>("Order added successfully", HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>("Order could not be added: " + e.getMessage(), e.getStatusCode());
        }

    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable("orderId") Long id,
            @RequestBody(required = false) Order order){
        try{
            orderService.updateOrder(id, order);
            return new ResponseEntity<>("Order updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
