package hu.pizzavalto.pizzaproject.pizza;

import hu.pizzavalto.pizzaproject.auth.AccessUtil;
import hu.pizzavalto.pizzaproject.model.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * PizzaKontroller osztály.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/pizza")
public class PizzaController {
    /**
     * PizzaService példányosítva.
     */
    private final PizzaService pizzaService;

    /**
     * PizzaKontroller konstruktora.
     *
     * @param pizzaService PizzaService típusú adatot vár.
     */
    @Autowired
    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
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
     * Ez a funkció az összes pizzát kilistázza GET.
     *
     * @return Pizza típusú listát ad vissza.
     */
    @GetMapping(path = "/get-all")
    public List<Pizza> getPizzas() {
        return pizzaService.getPizzas();
    }

    /**
     * Ez a funkció új pizzát ad hozzá a már meglévő pizza táblázatunkhoz POST.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param pizza         Pizza, amit fel szeretnénk tölteni.
     * @param authorization String token.
     * @return VálaszEntitást ad vissza amely a feltöltés állapotát tartalmazza.
     * Lejárt token esetén 451-es státuszkód.
     */
    @PostMapping(path = "/add-pizza")
    public ResponseEntity<?> addNewPizza(
            @RequestBody Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
            try {
                pizzaService.addNewPizza(pizza);
                return ResponseEntity.status(HttpStatus.OK).body("Pizza sikeresen feltöltve!");
            } catch (ResponseStatusException e) {
                return ResponseEntity.status(e.getStatusCode()).body("Pizza feltöltése sikertelen!");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
    }

    /**
     * Ez a funkció modósítja azt a pizzát, amelyiknek az azonosítoját megadjuk neki.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param pizzaId       Azt a pizza azonosítót kell megadni, amelyik pizzát változtatni szeretnénk.
     * @param pizza         Pizza típusú adat, amit változtatni akarunk az adott pizzán.
     * @param authorization Access token.
     * @return VálaszEntitást ad vissza amely tartalmazza a módosítás állapotát.
     * Lejárt token esetén 451-es státuszkód.
     */
    @PutMapping(path = "{pizzaId}")
    public ResponseEntity<?> updatePizza(
            @PathVariable("pizzaId") Long pizzaId,
            @RequestBody(required = false) Pizza pizza,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
            try {
                pizzaService.updatePizza(pizzaId, pizza);
                return ResponseEntity.status(HttpStatus.OK).body("Pizza sikeresen módosításra került!");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
    }
}
