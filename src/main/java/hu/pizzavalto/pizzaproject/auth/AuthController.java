package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.dto.UserRegisterDto;
import hu.pizzavalto.pizzaproject.model.Role;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Autentikáció kontroller osztálya.
 * Itt szerepelnek az autentikációval kapcsolatos endpointok.
 * Logn, Register, Admin-login, Refresh
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/auth")
public class AuthController {
    /**
     * Felhasználói Service fájl példányosítása.
     */
    private final UserService userService;
    /**
     * Biztonsági Service fájl példányosítása.
     */
    private final AuthService authService;

    /**
     * AuthKontroller konstruktorja.
     *
     * @param userService FelhasználóService típusú adat mellyel később hivatkozhasson rájuk az osztály más metódusaiban.
     * @param authService BiztonságiService típusú adat mellyel később hivatkozhasson rájuk az osztály más metódusaiban.
     */
    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * Ez a funkció felelős az felhasználó regisztrációjáért POST.
     *
     * @param user Felhasználó típusú adat.
     * @return VálaszEntitást ad vissza, amelyből kiderül, hogy sikeres volt-e a felhasználó létrehozása vagy sem. Illetve, hogy az e-mail cím használatban van -e.
     */
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody UserRegisterDto user) {
        try {
            User newUser = User.builder()
                    .first_name(user.getFirst_name())
                    .last_name(user.getLast_name())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(Role.USER)
                    .build();
            authService.addNewUser(newUser);
            return userService.createResponse(newUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Az Email cím használatban van!");
        }
    }

    /**
     * Ez a funkció Felelős a felhasználó bejelentkeztetéséért POST.
     * Sikeres azonosításnál tovább küldi a service osztályban szereplő createResponse metódusnak
     * ami elküldi a refresh token-t illetve az access token-t. Sikertelennél viszont a createErrorResponse
     * metódust hívja meg ami null értéket küld tokenek helyett.
     *
     * @param user Felhasználó típusú adat.
     * @return VálaszEntitást ad vissza, amelyből kiderül, hogy a felhasználó jó adatokkal probált-e belépni avagy sem.
     */
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> foundUser = authService.authUser(user.getEmail(), user.getPassword());
        return foundUser.map(userService::createResponse).orElseGet(userService::createErrorResponse);
    }

    /**
     * Ez a funkció Felelős az admin felhasználó bejelentkeztetéséért POST.
     * Sikeres azonosításnál tovább küldi a service osztályban szereplő createResponse metódusnak
     * ami elküldi a refresh token-t illetve az access token-t. Sikertelennél viszont a createErrorResponse
     * metódust hívja meg ami null értéket küld tokenek helyett.
     *
     * @param user Felhasználó típusú adat.
     * @return VálaszEntitást ad vissza, amelyből kiderül, hogy a felhasználó jó adatokkal probált-e belépni az
     * admin belépéskor avagy sem.
     */
    @PostMapping(path = "/admin-login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> adminLogin(@RequestBody User user) {
        Optional<User> foundUser = authService.authUser(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            if (foundUser.get().getRole().equals(Role.ADMIN)) {
                return userService.createResponse(foundUser.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Felhasználó nem található!");
        }
    }

    /**
     * Lejárt access token esetén új tokent kér a refresh tokenből POST.
     *
     * @param request refresh token.
     * @return VálaszEntitást ad vissza annak megfelőlen, hogy jó e a refresh token.
     */
    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest request) {
        if (RefreshUtil.isExpired(request.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String email = RefreshUtil.getEmailFromRefreshToken(request.getRefreshToken());
        User user = userService.findUserByEmail(email).orElse(null);
        if (user != null) {
            JwtResponse response = new JwtResponse(AccessUtil.createJWT(user), request.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            JwtResponse response = new JwtResponse(null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
