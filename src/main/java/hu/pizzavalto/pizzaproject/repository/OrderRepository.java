package hu.pizzavalto.pizzaproject.repository;

import hu.pizzavalto.pizzaproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
