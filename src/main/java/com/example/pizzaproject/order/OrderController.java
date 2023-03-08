package com.example.pizzaproject.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path="/get-all")
    public List<Order> getOrders(){
        List<Order> orders = orderService.getOrders();
        return orders;
    }

    @PostMapping(path = "/add-order")
    public ResponseEntity<String> addNewOrder(@RequestBody Order order){
        try{
            orderService.addNewOrder(order);
            return new ResponseEntity<>("Sikeres", HttpStatus.OK);
        } catch (ResponseStatusException e){
            return  new ResponseEntity<>("Nem sikerült", e.getStatusCode());
        }

    }

    @PutMapping(path = "{orderId}")
    public ResponseEntity<String> updateOrder(
            @PathVariable("orderId") Long id,
            @RequestBody(required = false) Order order){
        try{
            orderService.updateOrder(id, order);
            System.out.println(order.toString());
            return new ResponseEntity<>("Order updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
