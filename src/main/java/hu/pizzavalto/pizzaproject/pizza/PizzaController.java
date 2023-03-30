package hu.pizzavalto.pizzaproject.pizza;

import hu.pizzavalto.pizzaproject.auth.AccessUtil;
import hu.pizzavalto.pizzaproject.model.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/pizza")
public class PizzaController {

    private final PizzaService pizzaService;

    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    private String getToken(String authorization) {
        return authorization.substring(7);
    }

    @GetMapping(path = "/get-all")
    public List<Pizza> getPizzas() {
        return pizzaService.getPizzas();
    }

    @PostMapping(path = "/add-pizza")
    public ResponseEntity<?> addNewPizza(
            @RequestBody Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))){
            try {
                pizzaService.addNewPizza(pizza);
                return ResponseEntity.status(HttpStatus.OK).body("Pizza uploaded successfully");
            } catch (ResponseStatusException e) {
                return ResponseEntity.status(e.getStatusCode()).body("Pizza could not been uploaded");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to upload new pizza");
    }

    @PutMapping(path = "{pizzaId}")
    public ResponseEntity<?> updatePizza(
            @PathVariable("pizzaId") Long pizzaId,
            @RequestBody(required = false) Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))){
            try {
                pizzaService.updatePizza(pizzaId, pizza);
                return ResponseEntity.status(HttpStatus.OK).body("Pizza updated successfully");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
    }
}
