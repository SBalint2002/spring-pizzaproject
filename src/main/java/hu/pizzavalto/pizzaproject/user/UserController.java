package hu.pizzavalto.pizzaproject.user;

import hu.pizzavalto.pizzaproject.auth.AccessUtil;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.dto.UserResponseDto;
import hu.pizzavalto.pizzaproject.dto.UserUpdateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FelhasználóKontroller osztály.
 */
@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/user")
public class UserController {
    /**
     * FelhasználóService példányosítása.
     */
    private final UserService userService;

    /**
     * FelhasználóKontroller konstruktora.
     *
     * @param userService FelhasználóService adat.
     */
    public UserController(UserService userService) {
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
     * Ez a funkció az access tokenhez tartozó felhasználó adatait adja vissza GET.
     *
     * @param authorization Access token.
     * @return Felhasznó adatait küldi el.
     * Lejárt token esetén 451-es státuszkód.
     */
    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> getUserData(@RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        String email = AccessUtil.getEmailFromJWTToken(getToken(authorization));
        Optional<User> user = userService.findUserByEmail(email);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
                value.getId(),
                value.getFirst_name(),
                value.getLast_name(),
                value.getEmail(),
                value.getRole()))).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    /**
     * Ez a funkció kilistázza az adatbázisban megtalálható összes felhasználót GET.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param authorization Access token.
     * @return VálaszEntitást küld amiben szerepelhet a felhasználó típusú lista vagy a hibaüzenet.
     * Lejárt token esetén 451-es státuszkód.
     */
    @GetMapping(path = "/get-all")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)) {
            List<User> users = userService.getUsers();
            List<UserResponseDto> response = users.stream()
                    .map(user -> new UserResponseDto(user.getId(), user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getRole()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
    }

    /**
     * Ez a funkció törli ki az adatbázisból a megadot azonosítójú felhasználót DELETE.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param userId        Törlendő felhasználó azonosítója.
     * @param authorization Access token.
     * @return VálaszEntitást küld a törlés eredményéről.
     * Lejárt token esetén 451-es státuszkód.
     */
    @DeleteMapping(path = "{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body("Felhasználó sikeresen törlésre került!");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges!");
    }

    /**
     * Ez a funkció módosítja azt a felhasználót akinek az azonosítóját megkapja. PUT
     * Adminisztrátor felhasználó bármelyik másik felhasználót tudja módosítani.
     * Adminisztrátori joggal nem rendelkező felhasználó csak a saját adatait tudja módosítani.
     *
     * @param userId        Módodósítandó felhasználó azonosítója.
     * @param user          Módosítandó felhasználó típusú adatok.
     * @param authorization String token.
     * @return VálaszEntitást küld a módosítás eredményéről.
     * Lejárt token esetén 451-es státuszkód.
     */
    @PutMapping(path = "{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody(required = false) UserUpdateDto user,
            @RequestHeader("Authorization") String authorization) {
        if (AccessUtil.isExpired(getToken(authorization))) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        //Only admin
        if (AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
            try {
                userService.updateUserAdmin(userId, user);
                return ResponseEntity.status(HttpStatus.OK).body("Felhasználó módosítása sikeres!");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        //If not admin
        if (userId.equals(userService.getUserIdFromToken(getToken(authorization))) && !AccessUtil.isAdminFromJWTToken(getToken(authorization))) {
            try {
                userService.updateUser(userId, user);
                return ResponseEntity.status(HttpStatus.OK).body("Felhasználó módosítása sikeres!");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A hozzáféréshez Admin jogosultság szükséges, vagy csak a saját adatait tudja módosítani!");
    }
}
