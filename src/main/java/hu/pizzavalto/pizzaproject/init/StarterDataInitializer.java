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

@Component
public class StarterDataInitializer implements CommandLineRunner {
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PizzaRepository pizzaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataSource dataSource;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) {
        if (pizzaRepository.count() == 0) {
            try {
                ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data.sql"));
            } catch (SQLException e) {
                throw new RuntimeException("Failed to execute SQL script", e);
            }
        }
        if (userRepository.count() == 0){
            userRepository.save(new User(1L, "Elek", "Teszt", "tesztelek@gmail.com", passwordEncoder.encode("Adminadmin1"), Role.ADMIN));
        }
    }
}
