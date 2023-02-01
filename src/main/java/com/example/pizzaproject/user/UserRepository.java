package com.example.pizzaproject.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
//<User, Long> Student - Adattípus, Long - ID típusa
public interface UserRepository extends JpaRepository<User, Long> {

    //email alapú keresés (service addNewUser-ben felhasználva)
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findUserByEmail(String email);
}