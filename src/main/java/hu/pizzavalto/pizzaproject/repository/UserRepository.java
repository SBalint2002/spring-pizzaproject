package hu.pizzavalto.pizzaproject.repository;

import hu.pizzavalto.pizzaproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * FelhazsnálóRepository interface
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Adatbázis lekérdezést készít amiben email cím alapján keres egy felhasználót.
     *
     * @param email e-mail cím objektum.
     * @return Felhasználó típusú Optional.
     */
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);
}