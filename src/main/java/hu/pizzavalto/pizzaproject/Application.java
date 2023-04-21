package hu.pizzavalto.pizzaproject;

import hu.pizzavalto.pizzaproject.init.StarterDataInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * A Spring alkalmazás belépési pontja.
 * A @SpringBootApplication annotációval jelölt osztály.
 * A Spring Boot által biztosított SpringApplication futtatása, amely elindítja az alkalmazást.
 * A StarterDataInitializer bean-ként van definiálva, amely inicializálja a kezdeti adatokat az alkalmazás indításakor.
 */
@SpringBootApplication
public class Application {

    /**
     * Az alkalmazás belépési pontja, amely elindítja az alkalmazást a Spring Boot Application osztályának segítségével.
     *
     * @param args Parancssori argumentumokat tartalmazó String tömb.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * A StarterDataInitializer bean-ként van definiálva, amely inicializálja a kezdeti adatokat az alkalmazás indításakor.
     *
     * @return StarterDataInitializer metódus.
     */
    @Bean
    public StarterDataInitializer dataInitializer() {
        return new StarterDataInitializer();
    }
}
