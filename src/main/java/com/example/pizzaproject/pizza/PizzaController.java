package com.example.pizzaproject.pizza;

import com.example.pizzaproject.auth.AccessUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public ResponseEntity<?> addNewPizza(
            @RequestBody Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)){
            try {
                pizzaService.addNewPizza(pizza);
                return new ResponseEntity<>("Pizza uploaded successfully", HttpStatus.OK);
            } catch (ResponseStatusException e) {
                return new ResponseEntity<>("Pizza could not been uploaded", e.getStatusCode());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to upload new pizza");
    }

    @PutMapping(path = "{pizzaId}")
    public ResponseEntity<?> updatePizza(
            @PathVariable("pizzaId") Long pizzaId,
            @RequestBody(required = false) Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)){
            try {
                pizzaService.updatePizza(pizzaId, pizza);
                return new ResponseEntity<>("Pizza updated successfully", HttpStatus.OK);
            } catch (IllegalStateException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
    }
}
