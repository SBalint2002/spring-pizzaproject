package hu.pizzavalto.pizzaproject.repository;

import hu.pizzavalto.pizzaproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RendelésRepository interface.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Adatbázis lekérdezést hajt végre hogy visszakapja a felhasználóhoz tartozó rendeléseket.
     *
     * @param id Felhasználó azonosító.
     * @return Rendelés típusú lista.
     */
    List<Order> findOrdersByUserId(long id);
}
