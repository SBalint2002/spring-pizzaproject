package hu.pizzavalto.pizzaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

/**
 * Pizza osztály
 */
@Entity
@Table(name = "PIZZAS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pizza {
    /**
     * Magától generáltatott pizza id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Pizza neve.
     */
    @Column(name = "name")
    private String name;
    /**
     * Pizza ára.
     */
    @Column(name = "price")
    private int price;
    /**
     * Pizza leírása.
     */
    @Column(name = "description")
    private String description;
    /**
     * Pizza képe.
     */
    @Column(name = "picture")
    private String picture;
    /**
     * Pizza elérhetősége.
     */
    @Column(name = "available")
    private boolean available;
    /**
     * Köztes táblának a rendelésPizza pizza adatának összekapcsolásával tárolja EgyaTöbbhöz kapcsolattal.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "pizza")
    private List<OrderPizza> orderPizzas;
}
