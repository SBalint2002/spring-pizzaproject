package hu.pizzavalto.pizzaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="PIZZAS")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pizza {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private int price;
    @Column(name = "description")
    private String description;
    @Column(name = "picture")
    private String picture;

    @Column(name = "available")
    private boolean available;

    @JsonIgnore
    @OneToMany(mappedBy = "pizza")
    private List<OrderPizza> orderPizzas;

    public List<OrderPizza> getOrderPizzas() {
        return orderPizzas;
    }
}
