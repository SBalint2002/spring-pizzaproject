package hu.pizzavalto.pizzaproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Rendelés osztály.
 */
@Entity
@Table(name = "ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order implements Serializable {
    /**
     * Magától generáltatott rendelési id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Köztes táblának a felhasználói azonosítását tárolja TöbbazEgyhez kapcsolattal.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    /**
     * A rendelés helyszínét tároló változó.
     */
    @Column(name = "location")
    private String location;
    /**
     * A rendelés időpontját tároló változó.
     */
    @Column(name = "order_date")
    private Date order_date;
    /**
     * A rendelés árát tároló változó.
     */
    @Column(name = "price")
    private int price;
    /**
     * A rendelés telefonszámát tároló változó.
     */
    @Column(name = "phone_number")
    private String phone_number;
    /**
     * A rendelés státuszát tároló változó.
     */
    @Column(name = "ready")
    private boolean ready;
    /**
     * A Köztes táblát kapcsolja össze a Rendelés táblával EgyaTöbbhőz kapcsolattal.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderPizza> orderPizzas;

    /**
     * Ez a rendelés osztály konstruktora.
     *
     * @param user         Egy Felhasználó typusú adatot kér be, ami a rendeléshez hozzárendel egy felhasználót.
     * @param location     Egy String változót kér be, ami beállítja a rendelés helyszínét.
     * @param order_date   Egy Date változót kér be, ami beállítja a rendelés időpontját.
     * @param price        Egy Integer változót kér be, ami beállítja a rendelés árát.
     * @param phone_number Egy String változót kér be, ami beállítja a rendelés telefonszámát.
     * @param ready        Egy booleant kér be, ami beállítja a rendelés státuszát.
     */
    public Order(User user, String location, Date order_date, int price, String phone_number, boolean ready) {
        this.user = user;
        this.location = location;
        this.order_date = order_date;
        this.price = price;
        this.phone_number = phone_number;
        this.ready = ready;
        this.orderPizzas = new ArrayList<>();
    }

    /**
     * Ez a funkció adja oda a rendelésnek a rendelésben leadott pizzákat.
     *
     * @param pizzas Pizza típusú lista.
     */
    public void addPizzas(List<Pizza> pizzas) {
        for (Pizza pizza : pizzas) {
            orderPizzas.add(new OrderPizza(this, pizza));
        }
    }
}