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

    private final PizzaService pizzaService;

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
                return ResponseEntity.status(HttpStatus.OK).body("Pizza uploaded successfully");
            } catch (ResponseStatusException e) {
                return ResponseEntity.status(e.getStatusCode()).body("Pizza could not been uploaded");
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
                return ResponseEntity.status(HttpStatus.OK).body("Pizza updated successfully");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
    }
}
