package hu.pizzavalto.pizzaproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * ÚjRendelések osztály.
 */
@Entity
@Table(name = "NEW_ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewOrder implements Serializable {
    /**
     * Magától generáltatott újRendelési id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Köztes táblának a rendelési azonosítását tárolja többazEgyhez kapcsolattal.
     */
    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Order order;

    /**
     * Ez a funkció adja vissza a rendelést.
     * @return Visszaadja az az adott rendelést.
     */
    public Order getOrder() {
        return order;
    }
}
