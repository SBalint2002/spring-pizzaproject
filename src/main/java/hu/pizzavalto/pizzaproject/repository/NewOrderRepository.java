package hu.pizzavalto.pizzaproject.repository;

import hu.pizzavalto.pizzaproject.model.NewOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ÚjRendelésRepository interface.
 */
@Repository
public interface NewOrderRepository extends JpaRepository<NewOrder, Long> {
    /**
     * Adatbázis lekérdezést hajt végre hogy visszakapja a rendelést azonosító alapján.
     *
     * @param id Felhasználó azonosító.
     * @return Rendelés típusú lista.
     */
    Optional<NewOrder> findNewOrderById(Long id);
}
