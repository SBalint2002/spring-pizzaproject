package hu.pizzavalto.pizzaproject.init;

import hu.pizzavalto.pizzaproject.model.Role;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.repository.PizzaRepository;
import hu.pizzavalto.pizzaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Alkalmazás induláskor ellenőrzi, üres e az adatbázis, ha igen akkor feltölti alapértelmezett adatokkal:
 * - Egy sql fájlal ami tartalmaz 8 pizzát.
 * - 3 felhasználót amiből az egyik adminisztrátor.
 */
@Component
public class StarterDataInitializer implements CommandLineRunner {
    /**
     * Jelszó titkosítás.
     */
    private PasswordEncoder passwordEncoder;
    /**
     * Pizza Repository kapcsolata az adatbázishoz.
     */
    @Autowired
    private PizzaRepository pizzaRepository;
    /**
     * Felhasználó Repository kapcsolata az adatbázishoz.
     */
    @Autowired
    private UserRepository userRepository;
    /**
     * SQL fájl összekapcsolására alakalmas Objektum.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Jelszó titkosítás.
     *
     * @param passwordEncoder Jelszót titkosító Objektum.
     */
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Backend indításakor, hogyha nincs adatbázisban találkható adat akkor létrehoz előre megadott adatokat.
     *
     * @param args Argumentum.
     */
    @Override
    public void run(String... args) {
        if (pizzaRepository.count() == 0) {
            try {
                ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data.sql"));
            } catch (SQLException e) {
                throw new RuntimeException("SQL fájl futtatása sikertelen volt!", e);
            }
        }
        if (userRepository.count() == 0) {
            userRepository.save(new User(1L, "Elek", "Teszt", "tesztelek@gmail.com", passwordEncoder.encode("Adminadmin1"), Role.ADMIN));
            userRepository.save(new User(2L, "Béla", "Teszt", "tesztbela@gmail.com", passwordEncoder.encode("Adminadmin1"), Role.USER));
            userRepository.save(new User(3L, "János", "Teszt", "tesztjanos@gmail.com", passwordEncoder.encode("Adminadmin1"), Role.USER));
        }
    }
}
