package com.example.pizzaproject.pizza;

import com.example.pizzaproject.auth.JwtResponse;
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

    @GetMapping(path="/get-all")
    public List<Pizza> getPizzas(){
        List<Pizza> pizzas = pizzaService.getPizzas();
        return pizzas;
    }

    @PostMapping(path = "/add-pizza")
    public ResponseEntity<String> addNewPizza(@RequestBody Pizza pizza){
        try{
            pizzaService.addNewPizza(pizza);
            return new ResponseEntity<>("Sikeres",HttpStatus.OK);
        } catch (ResponseStatusException e){
            return  new ResponseEntity<>("Nem sikerült", e.getStatusCode());
        }

    }

    @PostMapping(path = "{pizzaId}")
    public ResponseEntity<String> updatePizza(
            @PathVariable("pizzaId") Long pizzaId,
            @RequestBody(required = false) Pizza pizza){
        try{
            pizzaService.updatePizza(pizzaId, pizza);
            System.out.println(pizza.toString());
            return new ResponseEntity<>("Pizza updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "{pizzaId}")
    public ResponseEntity<String> deletePizza(@PathVariable("pizzaId") Long pizzaId) {
        pizzaService.deletePizza(pizzaId);
        return new ResponseEntity<>("Pizza deleted successfully", HttpStatus.OK);
    }
}
