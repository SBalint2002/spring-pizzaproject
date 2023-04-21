package hu.pizzavalto.pizzaproject.user;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import hu.pizzavalto.pizzaproject.auth.JwtResponse;
import hu.pizzavalto.pizzaproject.auth.AccessUtil;
import hu.pizzavalto.pizzaproject.auth.RefreshUtil;
import hu.pizzavalto.pizzaproject.dto.UserUpdateDto;
import hu.pizzavalto.pizzaproject.model.Role;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static hu.pizzavalto.pizzaproject.auth.AccessUtil.getEmailFromJWTToken;

/**
 * FelhasználóService osztály.
 */
@Service
public class UserService {
    /**
     * FelhasználóRepository példányosítása.
     */
    private final UserRepository userRepository;
    /**
     * JelszóEnkóder példányosítása.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * FelhasználóService konstruktora.
     *
     * @param userRepository  FelhasználóRepository típusú adat.
     * @param passwordEncoder JelszóEnkóder típusú adat.
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Ez a funkció adja vissza az összes felhasználónak a listáját.
     *
     * @return Flehasználó típusú listát ad vissza.
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Ez a funkció adja meg, a tokenhez tartó felhasználó azonosítóját.
     *
     * @param token String token.
     * @return Felhasználó azonosítóját adja vissza.
     */
    public Long getUserIdFromToken(String token) {
        Optional<User> user = findUserByEmail(getEmailFromJWTToken(token));
        return user.map(User::getId).orElse(null);
    }

    /**
     * Ez a funkció email címből visszaadja, hogy melyik felhasználóé az adott email cím.
     *
     * @param email E-mail cím.
     * @return Felhasználó Optional objektumot ad vissza.
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * Ez a funkció felhasználó adataiból létrehozza az access és a refresh tokent.
     *
     * @param user Felhasználó típusú adatot vár.
     * @return VálaszEntitást ad vissza.
     */
    public ResponseEntity<JwtResponse> createResponse(User user) {
        String accessToken = AccessUtil.createJWT(user);
        String refreshToken = RefreshUtil.createRefreshToken(user);
        JwtResponse response = new JwtResponse(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Ez a funkció vissza küld null értéket belépéskor, ha rosszak a bevitt adatok.
     *
     * @return VálaszEntitást ad vissza.
     */
    public ResponseEntity<JwtResponse> createErrorResponse() {
        JwtResponse response = new JwtResponse(null, null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Ez a funkció az adott azonosítóval rendelkező felhasználót kitörli.
     *
     * @param userId Felhasználó azonosítót vár.
     */
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("Felhasználó " + userId + " azonosítóval nem létezik!");
        }
        userRepository.deleteById(userId);
    }

    /**
     * Ez a funkció frissíti a felhasználó szerepkörét.
     * És tovább adja az updateUserInformation metódusnak a felhasználót.
     * Csak adminisztrátor férhet hozzá.
     *
     * @param userId     Az adott felhasználó azonosítója.
     * @param updateUser Az adott felhasználó további változtatásra váro adatai.
     */
    @Transactional
    public void updateUserAdmin(Long userId, UserUpdateDto updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Felhasználó " + userId + " azonosítóval nem létezik!"));
        updateUserInformation(user, updateUser);
        if (updateUser.getRole() == Role.ADMIN) {
            user.setRole(Role.ADMIN);
        }
        if (updateUser.getRole() == Role.USER) {
            user.setRole(Role.USER);
        }
    }

    /**
     * Ez a funkció hívja meg az updateUserInformation functions, ami beállítja a felhasználói módosításokat.
     * Ezt a nem adminisztrátor használja.
     *
     * @param userId     Az adott felhasználó azonosítója.
     * @param updateUser Az adott felhasználó további változtatásra váro adatai.
     */
    @Transactional
    public void updateUser(Long userId, UserUpdateDto updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Felhasználó " + userId + " azonosítóval nem létezik!"));
        updateUserInformation(user, updateUser);
    }

    /**
     * Ez a funkció tudja megváltoztatni az adott felhasználó adatait.
     *
     * @param user       Az adott felhasználó azonosítója.
     * @param updateUser Az adott felhasználó módosított adatai.
     */
    @Transactional
    private void updateUserInformation(User user, UserUpdateDto updateUser) {
        if (updateUser.getFirst_name() != null && updateUser.getFirst_name().length() > 0) {
            user.setFirst_name(updateUser.getFirst_name());
        }
        if (updateUser.getLast_name() != null && updateUser.getLast_name().length() > 0) {
            user.setLast_name(updateUser.getLast_name());
        }
        if (updateUser.getEmail() != null && updateUser.getEmail().length() > 0 && !Objects.equals(updateUser.getEmail(), user.getEmail())) {
            Optional<User> userOptional = userRepository.findUserByEmail(updateUser.getEmail());
            if (userOptional.isPresent()) {
                throw new IllegalStateException("Email cím foglalt!");
            }
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null && updateUser.getPassword().length() > 6) {
            user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
    }
}