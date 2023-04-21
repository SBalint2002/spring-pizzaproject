package hu.pizzavalto.pizzaproject.order;

import hu.pizzavalto.pizzaproject.model.NewOrder;
import hu.pizzavalto.pizzaproject.model.Order;
import hu.pizzavalto.pizzaproject.model.Pizza;
import hu.pizzavalto.pizzaproject.repository.PizzaRepository;
import hu.pizzavalto.pizzaproject.repository.NewOrderRepository;
import hu.pizzavalto.pizzaproject.repository.OrderRepository;
import hu.pizzavalto.pizzaproject.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * RendeléService osztály.
 */
@Service
public class OrderService {
    /**
     * RendelésRepository példányosítása.
     */
    private final OrderRepository orderRepository;
    /**
     * FelhasználóService példányosítása.
     */
    private final UserService userService;
    /**
     * pizzaRepository példányosítása.
     */
    private final PizzaRepository pizzaRepository;
    /**
     * ÚjRendelésRepository példányosítása.
     */
    private final NewOrderRepository newOrderRepository;

    /**
     * RendeléService konstruktora.
     *
     * @param orderRepository    RendelésRepository típusú adat.
     * @param userService        FelhasználóService típusú adat.
     * @param pizzaRepository    pizzaRepository típusú adat.
     * @param newOrderRepository ÚjRendelésRepository típusú adat.
     */
    @Autowired
    public OrderService(OrderRepository orderRepository, UserService userService, PizzaRepository pizzaRepository, NewOrderRepository newOrderRepository) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.pizzaRepository = pizzaRepository;
        this.newOrderRepository = newOrderRepository;
    }

    /**
     * Ez a funkció kiszedi a rendeléseket az új rendelésekből azonosítok alapján.
     *
     * @return Rendelés típusú listát ad vissza.
     */
    public List<Order> getOrdersByIdsInNewOrders() {
        List<NewOrder> newOrders = newOrderRepository.findAll();
        List<Long> orderIds = newOrders.stream()
                .map(no -> no.getOrder().getId())
                .collect(Collectors.toList());
        return orderRepository.findAllById(orderIds);
    }

    /**
     * Ez a funkció egy új rendelést tölt fel az adatbázisba.
     *
     * @param order Rendelés típusú adat.
     */
    @Transactional
    public void addNewOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        NewOrder newOrder = new NewOrder();
        newOrder.setOrder(savedOrder);
        newOrderRepository.save(newOrder);
    }

    /**
     * Ez a funkció összesíti a Rendelésben megtalálható piizák összegét.
     *
     * @param pizzaIds Pizza azonosító típusú lista.
     * @return Integert ad vissza a rendelésben szereplő összepizza összáráról.
     */
    public int sumPrice(List<Long> pizzaIds) {
        var sum = 0;
        for (Long pizzaId : pizzaIds) {
            Pizza pizza = pizzaRepository.findById(pizzaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza nem található!"));
            sum += pizza.getPrice();
        }
        return sum;
    }

    /**
     * Ez a funkció visszaadja a felhasználóhoz tartozó rendeléseket..
     *
     * @param token Access token.
     * @return Rendelések listáját adja vissza.
     */
    public List<Order> getOrderById(String token) {
        return orderRepository.findOrdersByUserId(userService.getUserIdFromToken(token));
    }

    /**
     * Ez a funkció modósítja az adott rendelés adatait.
     *
     * @param orderId     Rendelésnek az azonosítoja.
     * @param updateOrder Rendelésnek az adatait várja, amiket át kell, hogy állítson.
     */
    @Transactional
    public void updateOrder(Long orderId, Order updateOrder) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException("Rendelés " + orderId + " azonosítóval nem létezik!"));

        if (updateOrder.isReady()) {
            order.setReady(true);
        }
        if (!updateOrder.isReady()) {
            order.setReady(false);
        }

        Optional<NewOrder> optionalNewOrder = newOrderRepository.findNewOrderById(orderId);
        optionalNewOrder.ifPresent(newOrderRepository::delete);
    }
}
