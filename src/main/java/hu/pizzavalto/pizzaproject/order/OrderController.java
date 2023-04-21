package hu.pizzavalto.pizzaproject.order;

import hu.pizzavalto.pizzaproject.auth.AccessUtil;
import hu.pizzavalto.pizzaproject.model.Order;
import hu.pizzavalto.pizzaproject.model.Pizza;
import hu.pizzavalto.pizzaproject.dto.OrderDto;
import hu.pizzavalto.pizzaproject.repository.PizzaRepository;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

/**
 * RendelésiKontroller osztály.
 */
@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/order")
public class OrderController {
    /**
     * RendelésiService példányosítása.
     */
    private final OrderService orderService;
    /**
     * PizzaRepository példányosítása.
     */
    private final PizzaRepository pizzaRepository;
    /**
     * FelhasználóiService példányosítása.
     */
    private final UserService userService;

    /**
     * RendelésiKontroller konstruktor.
     *
     * @param orderService    RendelésiService típusú változót vár.
     * @param pizzaRepository PizzaRepository típusú változót vár.
     * @param userService     FelhasználóiService típusú változót vár.
     */
    @Autowired
    public OrderController(OrderService orderService, PizzaRepository pizzaRepository, UserService userService) {
        this.orderService = orderService;
        this.pizzaRepository = pizzaRepository;
        this.userService = userService;
    }

    /**
     * A request headerjében kapunk egy tokent, de előtte szerepel a "Bearer " szó
     * így ez a metódus kiveszi előle és viszaadja csak a token-t.
     *
     * @param authorization Nyers tokent tartalmazó String.
     * @return Stringből csakis a tokent adja vissza.
     */
    private String getToken(String authorization) {
        return authorization.substring(7);
    }

    /**
     * Ez a funkció adja vissza az adott felhasználóhoz tartozó összes rendelést az adatbázisból GET.
     *
     * @param authorization Access token.
     * @return VálaszEntitást ad vissza amiben szerepelhetnek a rendelések vagy akár hibakód.
     * Lejárt token esetén 451-es státuszkód.
     */
    @GetMapping(path = "/get-orders")
    public ResponseEntity<?> getOrdersById(@RequestHeader("Authorization") String authorization) {
        try {
            if (AccessUtil.isExpired(getToken(authorization))) {
                //status code 451
                return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(getToken(authorization)));
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return a 500 error with the exception message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Ez a funkció adja vissza az összes új rendelést ami nincs készre állítva az adatbázisból GET.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param authorization Access token.
     * @return VálaszEntitást ad vissza amiben szerepelhetnek a rendelések vagy akár hibakód.
     * Lejárt token esetén 451-es státuszkód.
     */
    @GetMapping(path = "/get-new-orders")
    public ResponseEntity<?> getNewOrders(@RequestHeader("Authorization") String authorization) {
        try {
            if (AccessUtil.isExpired(getToken(authorization))) {
                //status code 451
                return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
            }
            if (AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
                return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrdersByIdsInNewOrders());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return a 500 error with the exception message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Ez a funkció ad új rendelést hozzá a már meglévő rendeléseinkhez POST.
     *
     * @param orderDto      Rendelési DataTransferObjektum.
     * @param authorization Access token.
     * @return VálaszEntitást ad vissza.
     * Lejárt token esetén 451-es státuszkód.
     */
    @PostMapping(path = "/add-order")
    public ResponseEntity<String> addNewOrder(
            @RequestBody OrderDto orderDto,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization)) || getToken(authorization).isEmpty()) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        try {
            String email = AccessUtil.getEmailFromJWTToken(getToken(authorization));
            Optional<User> user = userService.findUserByEmail(email);
            int price = orderService.sumPrice(orderDto.getPizzaIds());
            if (user.isPresent()) {
                List<Pizza> pizzas = pizzaRepository.findAllById(orderDto.getPizzaIds());
                Set<Long> ids = new HashSet<>(orderDto.getPizzaIds());
                if (ids.size() != pizzas.size()) {
                    log.info("Error {} {}", pizzas.size(), orderDto.getPizzaIds().size());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza nem található");
                }
                // Annyiszor rakja bele az adott pizzát ahányszor szerepel a listában a megadott id. [1,1,1] esetén 3x kerül bele az 1-es id-jú pizza
                List<Pizza> orderedPizzas = new ArrayList<>();
                for (Pizza pizza : pizzas) {
                    for (int j = 0; j < orderDto.getPizzaIds().size(); j++) {
                        if (Objects.equals(pizza.getId(), orderDto.getPizzaIds().get(j))) {
                            orderedPizzas.add(pizza);
                        }
                    }
                }
                Order order = new Order(user.get(), orderDto.getLocation(), new Date(), price, orderDto.getPhoneNumber(), false);
                order.addPizzas(orderedPizzas);
                orderService.addNewOrder(order);
                return ResponseEntity.status(HttpStatus.OK).body("Rendelés sikeresen hozzáadva!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Felhasználó nem található!");
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Rendelés sikertelen: " + e.getMessage());
        }
    }

    /**
     * Ez a funkció módosítja a megadott rendelést PUT.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param id            Rendelés id.
     * @param order         Rendelés típusú adat, amire változtatjuk az adatbázisban szereplő rendelést..
     * @param authorization Access token.
     * @return VálaszEntitást ad vissza amiben szerepel a módosítás állapota.
     * Lejárt token esetén 451-es státuszkód.
     */
    @PutMapping(path = "{orderId}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("orderId") Long id,
            @RequestBody(required = false) Order order,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
            try {
                orderService.updateOrder(id, order);
                return ResponseEntity.status(HttpStatus.OK).body("Rendelés módosítása sikeres volt!");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
    }
}
