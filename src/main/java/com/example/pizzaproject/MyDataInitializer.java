package com.example.pizzaproject;

import com.example.pizzaproject.pizza.PizzaRepository;
import com.example.pizzaproject.user.User;
import com.example.pizzaproject.user.UserRepository;
import com.example.pizzaproject.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class MyDataInitializer implements CommandLineRunner {
    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        if (pizzaRepository.count() == 0) {
            try {
                ScriptUtils.executeSqlScript(dataSource.getConnection(), new ClassPathResource("data.sql"));
            } catch (SQLException e) {
                throw new RuntimeException("Failed to execute SQL script", e);
            }
        }
    }
}
