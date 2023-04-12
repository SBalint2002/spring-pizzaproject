package hu.pizzavalto.pizzaproject.repository;

import hu.pizzavalto.pizzaproject.model.Order;
import hu.pizzavalto.pizzaproject.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(long id);
}
