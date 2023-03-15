package com.example.pizzaproject.pizza;

import com.example.pizzaproject.auth.JwtUtil;
import com.example.pizzaproject.user.User;
import com.example.pizzaproject.user.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/pizza")
public class PizzaController {

    @Autowired
    private PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping(path = "/get-all")
    public List<Pizza> getPizzas() {
        return pizzaService.getPizzas();
    }

    @PostMapping(path = "/add-pizza")
    public ResponseEntity<String> addNewPizza(
            @RequestBody Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (JwtUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        try {
            pizzaService.addNewPizza(pizza);
            return new ResponseEntity<>("Sikeres", HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>("Nem sikerült", e.getStatusCode());
        }

    }

    @PutMapping(path = "{pizzaId}")
    public ResponseEntity<String> updatePizza(
            @PathVariable("pizzaId") Long pizzaId,
            @RequestBody(required = false) Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (JwtUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        try {
            pizzaService.updatePizza(pizzaId, pizza);
            System.out.println(pizza.toString());
            return new ResponseEntity<>("Pizza updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "{pizzaId}")
    public ResponseEntity<String> deletePizza(
            @PathVariable("pizzaId") Long pizzaId,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (JwtUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        pizzaService.deletePizza(pizzaId);
        return new ResponseEntity<>("Pizza deleted successfully", HttpStatus.OK);
    }
}
