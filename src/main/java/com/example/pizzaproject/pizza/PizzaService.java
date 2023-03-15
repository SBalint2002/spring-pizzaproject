package com.example.pizzaproject.pizza;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class PizzaService {
    private final PizzaRepository pizzaRepository;

    @Autowired
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    public List<Pizza> getPizzas() {
        return pizzaRepository.findAll();
    }

    public void addNewPizza(Pizza pizza){
        Optional<Pizza> existingPizza = pizzaRepository.findPizzaByName(pizza.getName());
        if (existingPizza.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pizza is already in the database");
        }
        pizzaRepository.save(pizza);
    }

    @Transactional
    public void updatePizza(Long pizzaId, Pizza updatePizza) {
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalStateException("Pizza with id " + pizzaId + " does not exist"));

        if (updatePizza.getName() != null && updatePizza.getName().length() > 0){
            Optional<Pizza> pizzaOptional = pizzaRepository.findPizzaByName(updatePizza.getName());
            if (pizzaOptional.isPresent()){
                throw new IllegalStateException("Pizza name is taken");
            }
            pizza.setName(updatePizza.getName());
        }

        if (updatePizza.getPrice() > 0){
            pizza.setPrice(updatePizza.getPrice());
        }

        if (updatePizza.getDescription() != null && updatePizza.getDescription().length() > 0){
            pizza.setDescription(updatePizza.getDescription());
        }

        if (updatePizza.getPicture() != null && updatePizza.getPicture().length() > 0){
            pizza.setPicture(updatePizza.getPicture());
        }

        if (updatePizza.isAvailable()) {
            pizza.setAvailable(true);
        }
        if (!updatePizza.isAvailable()) {
            pizza.setAvailable(false);
        }
    }
    public void deletePizza(long pizzaId){
        if (!pizzaRepository.existsById(pizzaId)) {
            throw new IllegalStateException("pizza with id " + pizzaId + " does not exist");
        }
       pizzaRepository.deleteById(pizzaId);
    }
}
