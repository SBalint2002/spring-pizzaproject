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
    /**
     * Adatbázis lekérdezést hajt végre amivel pizza név alapján keresi meg a Pizza Objektumot.
     *
     * @param name Pizza neve.
     * @return Pizza típusú Optional.
     */
    @Query("SELECT u FROM Pizza u WHERE u.name = ?1")
    Optional<Pizza> findPizzaByName(String name);
}