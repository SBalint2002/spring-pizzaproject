package hu.pizzavalto.pizzaproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * NewOrder osztály, amely tárolja az új rendelés azonosítóját
 * illetve össze van kötve a rendelések táblával ManyToOne kapcsolással az order_id-n.
 */
@Entity
@Table(name = "NEW_ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewOrder implements Serializable {
    /**
     * Új rendelés azonosító.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Össze van kötve a rendelések táblával ManyToOne kapcsolással az order_id-n.
     */
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    /**
     * Ez a funkció adja vissza a rendelést.
     * @return rendelés.
     */
    public Order getOrder() {
        return order;
    }
}
