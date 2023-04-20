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

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/order")
public class OrderController {
    private final OrderService orderService;
    private final PizzaRepository pizzaRepository;

    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, PizzaRepository pizzaRepository, UserService userService) {
        this.orderService = orderService;
        this.pizzaRepository = pizzaRepository;
        this.userService = userService;
    }

    private String getToken(String authorization) {
        return authorization.substring(7);
    }

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


    @PostMapping(path = "/add-order")
    public ResponseEntity<String> addNewOrder(
            @RequestBody OrderDto orderDto,
            @RequestHeader("Authorization") String authorization) {
            log.info(orderDto.toString());
            log.info(authorization);
        if (AccessUtil.isExpired(getToken(authorization)) || getToken(authorization).isEmpty()) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        try {
            String email = AccessUtil.getEmailFromJWTToken(getToken(authorization));
            Optional<User> user = userService.findUserByEmail(email);
            int price = orderService.sumPrice(orderDto.getPizzaIds());
            if (user.isPresent()){
                List<Pizza> pizzas = pizzaRepository.findAllById(orderDto.getPizzaIds());
                Set<Long> ids = new HashSet<>(orderDto.getPizzaIds());
                if (ids.size() != pizzas.size()) {
                    log.info("Error {} {}", pizzas.size(), orderDto.getPizzaIds().size());
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza nem található");
                }
                Order order = new Order(
                        user.get().getId(),
                        orderDto.getLocation(),
                        new Date(),
                        price,
                        orderDto.getPhoneNumber(),
                        false);
                order.addPizzas(pizzas);
                orderService.addNewOrder(order);
                return ResponseEntity.status(HttpStatus.OK).body("Rendelés sikeresen hozzáadva!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Felhasználó nem található!");
            }
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Rendelés sikertelen: " + e.getMessage());
        }
    }


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
