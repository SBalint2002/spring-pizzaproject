package com.example.pizzaproject.order;

import com.example.pizzaproject.auth.AccessUtil;
import com.example.pizzaproject.pizza.Pizza;
import com.example.pizzaproject.pizza.PizzaRepository;
import com.example.pizzaproject.user.User;
import com.example.pizzaproject.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/order")
public class OrderController {
    private final OrderService orderService;
    private final PizzaRepository pizzaRepository;

    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, PizzaRepository pizzaRepository, UserService userService) {
        this.orderService = orderService;
        this.pizzaRepository = pizzaRepository;
        this.userService = userService;
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<?> getOrders(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token) || token.isEmpty()) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
    }


    @PostMapping(path = "/add-order")
    public ResponseEntity<String> addNewOrder(
            @RequestBody OrderDto orderDto,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token) || token.isEmpty()) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        try {
            String email = AccessUtil.getEmailFromJWTToken(token);
            Optional<User> user = userService.findUserByEmail(email);
            int price = orderService.sumPrice(orderDto.getPizzaIds());
            if (user.isPresent()){
                Order order = new Order(
                        user.get().getId(),
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
                return ResponseEntity.status(HttpStatus.OK).body("Order added successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Order could not be added: " + e.getMessage());
        }
    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("orderId") Long id,
            @RequestBody(required = false) Order order,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)) {
            try {
                orderService.updateOrder(id, order);
                return ResponseEntity.status(HttpStatus.OK).body("Order updated successfully");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to modify order");
    }
}
