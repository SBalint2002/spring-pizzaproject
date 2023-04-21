package hu.pizzavalto.pizzaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Rendelés és pizza között összeköttetést nyújtó tábla.
 * Lehetővé teszi, hogy egy pizzából több szerepelhessen egy rendelésben.
 */
@Entity
@Table(name = "ORDER_PIZZA")
public class OrderPizza {
    /**
     * Magától generáltatott rendelésPizza id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Köztes táblának a rendelési azonosítását tárolja. TöbbazEgyhez táblakapcsolattal.
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;
    /**
     * Köztes táblának a pizza azonosítását tárolja. TöbbazEgyhez táblakapcsolattal.
     */
    @ManyToOne
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;

    /**
     * Üres konstruktor
     */
    public OrderPizza() {
    }

    /**
     * RendelésPizza Konstruktora rendelés és pizza osztályok felhasználásával.
     *
     * @param order Rendelési típusú adatot vár, amivel példányosítja a rendelésPizza osztályt.
     * @param pizza Pizza típusú adatot vár, amivel példányosítja a rendelésPizza osztályt.
     */
    public OrderPizza(Order order, Pizza pizza) {
        this.order = order;
        this.pizza = pizza;
    }

    /**
     * Visszaadja a rendelésPizza azonosítóját.
     *
     * @return Az adott rendelésPizza azonosítóját.
     */
    public Long getId() {
        return id;
    }

    /**
     * Beállítja a rendelésPizza azonosítóját a kapott értékre.
     *
     * @param id Új azonosítót vár.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Ez a funkció visszaadja az adott rendelésPizza pizza elemét.
     *
     * @return Pizza típusú adat.
     */
    public Pizza getPizza() {
        return pizza;
    }

    /**
     * Ez a funkció megváltoztatja az adott rendelésPizza pizza elemét.
     *
     * @param pizza Pizza típusú adatot vár majd arra cseréli a már fent lévő pizza tartalmát.
     */
    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    /**
     * Ez a funkció adja vissza a rendelésPizza elemét Stringként.
     *
     * @return Stringként visszaadja.
     */
    @Override
    public String toString() {
        return "OrderPizza{" +
                "id=" + id +
                ", order=" + order +
                ", pizza=" + pizza +
                '}';
    }
}
