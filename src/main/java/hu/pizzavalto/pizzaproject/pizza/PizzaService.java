package hu.pizzavalto.pizzaproject.pizza;

import hu.pizzavalto.pizzaproject.model.Pizza;
import hu.pizzavalto.pizzaproject.repository.PizzaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * PizzaService osztály.
 */
@Service
public class PizzaService {
    /**
     * PizzaRepository példányosítása.
     */
    private final PizzaRepository pizzaRepository;
    /**
     * PizzaService konstruktora.
     * @param pizzaRepository PizzaRepository típusú adat.
     */
    @Autowired
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }
    /**
     * Ez a funkció az adatbázisból visszaadja az összes pizzát.
     * @return Pizza típusú listát ad vissza.
     */
    public List<Pizza> getPizzas() {
        return pizzaRepository.findAll();
    }
    /**
     * Ez a funkció egy új pizzát hozz létre az adatbázisba.
     * @param pizza Létrehozando pizza paraméterei Pizza típusúként.
     */
    public void addNewPizza(Pizza pizza){
        Optional<Pizza> existingPizza = pizzaRepository.findPizzaByName(pizza.getName());
        if (existingPizza.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pizza is already in the database");
        }
        pizzaRepository.save(pizza);
    }

    /**
     * Ez a funkció az adatbázisban lévő egyik pizzát módosítja
     * @param pizzaId Azt a pizza azonosított várja amelyik pizzán változtatni szeretnénk.
     * @param updatePizza Pizza típusban várja azt, hogy a pizzán mit kell, hogy módosítson.
     */
    @Transactional
    public void updatePizza(Long pizzaId, Pizza updatePizza) {
        Pizza pizza = pizzaRepository.findById(pizzaId)
                .orElseThrow(() -> new IllegalStateException("Pizza with id " + pizzaId + " does not exist"));

        if (updatePizza.getName() != null && updatePizza.getName().length() > 0 && !Objects.equals(updatePizza.getName(), pizza.getName())){
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
}
