package hu.pizzavalto.pizzaproject.repository;

import hu.pizzavalto.pizzaproject.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * PizzaRepository inteface.
 */
@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    @Query("SELECT u FROM Pizza u WHERE u.name = ?1")
    Optional<Pizza> findPizzaByName(String name);
}