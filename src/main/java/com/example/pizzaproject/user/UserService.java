package com.example.pizzaproject.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    //UserRepository meghívása, alatta kontruktora
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //Get metódus, listában közli a választ
    public List<User> getUsers() {
        return userRepository.findAll();
    }


    //Új felhasználó hozzáadása és egyedi e-mail cím ellenőrzése
    public void addNewUser(User user) {
        Optional<User> studentOptional = userRepository.findUserByEmail(user.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Email is taken");
        }
        userRepository.save(user);
    }


    //Felhasználó törlése
    public void deleteUser(Long userId) {
        //Szerepel e az adatbázisban true/false
        boolean exists = userRepository.existsById(userId);
        if (!exists) {
            throw new IllegalStateException("User with id " + userId + " does not exists!");
        }
        userRepository.deleteById(userId);
    }

    //Felhasználó adatainak módosítása
    @Transactional
    public void updateUser(Long userId, String first_name, String last_name, String email, boolean admin, boolean cook) {
        //Ellenőrzi, hogy van -e mit updatelni
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException(
                "User with id " + userId + " does not exists"
        ));

        //Megfelel -e a követelményeknek
        if (first_name != null && first_name.length() > 0 && !Objects.equals(user.getFirst_name(), first_name)) {
            user.setFirst_name(first_name);
        }

        if (last_name != null && last_name.length() > 0 && !Objects.equals(user.getLast_name(), last_name)) {
            user.setLast_name(last_name);
        }

        if (email != null && email.length() > 0 && !Objects.equals(user.getEmail(), email)) {
            Optional<User> studentOptional = userRepository.findUserByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("Email is taken");
            }
            user.setEmail(email);
        }

        if (!Objects.equals(user.isAdmin(), admin)){
            user.setAdmin(admin);
        }

        if (!Objects.equals(user.isCook(), cook)){
            user.setCook(cook);
        }
    }
}
