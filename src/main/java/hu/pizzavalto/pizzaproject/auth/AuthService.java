package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.model.Role;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * AuthService osztály amely az authentikációhoz tartozó szolgáltatásokat tartalmazza.
 */
@Service
public class AuthService {
    /**
     * FelhasználóRepositori példányosítása.
     */
    private final UserRepository userRepository;
    /**
     * JelszóEnkóder példányosítása.
     */
    private final PasswordEncoder passwordEncoder;
    /**
     * AuthService konstruktorja.
     * @param userRepository FelhasználóRepositori típusú adat mellyel később hivatkozhasson rájuk az osztály más metódusaiban.
     * @param passwordEncoder JelszóEnkóder típusú adat mellyel később hivatkozhasson rájuk az osztály más metódusaiban.
     */
    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Ez a funkció ad új felhasználót hozzá az adatbázishoz.
     * @param user Felhasználó típusú adat.
     * @throws ResponseStatusException Akkor dob ResponseStatusException-t, ha az adott email már foglalt.
     */
    public void addNewUser(User user) throws ResponseStatusException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Az Email cím már használatban van!");
        }
        user.setRole(Role.USER);
        userRepository.save(user);
    }
    /**
     * Ez a funkció felelős a felhasználó beléptetéséért.
     * @param email Felhasználó emailje Stringként.
     * @param password Felhasználó jelszava Stringként.
     * @return Amennyiben jó adatokat adunk meg visszakapunk egy felhasználót.
     */
    public Optional<User> authUser(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
}
