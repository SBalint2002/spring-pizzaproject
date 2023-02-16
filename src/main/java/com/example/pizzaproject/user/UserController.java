package com.example.pizzaproject.user;

import com.example.pizzaproject.auth.JwtResponse;
import com.example.pizzaproject.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/get-all")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<JwtResponse> registerNewUser(@RequestBody User user) {
        try{
            userService.addNewUser(user);
            String token = JwtUtil.createJWT(user);
            JwtResponse response = new JwtResponse("User created successfully", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            JwtResponse response = new JwtResponse("Error while creating user", null);
            return new ResponseEntity<>(response, e.getStatusCode());
        }
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> login(@RequestBody User user) {
        Optional<User> foundUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            String jwtToken = JwtUtil.createJWT(foundUser.get());
            JwtResponse response = new JwtResponse("success",jwtToken);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            System.out.println("Email-jelszó páros nem passzol");
            JwtResponse response = new JwtResponse("failure", null);
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserData(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        String email = JwtUtil.getEmailFromToken(token);
        Optional<User> user = userService.findUserByEmail(email);
        if (user.isPresent()) {
            User userData = new User(user.get().getId(), user.get().getFirst_name(), user.get().getLast_name(), user.get().getEmail(), user.get().getPassword(), user.get().isAdmin());
            System.out.println(userData);
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/email")
    public String getEmailByToken(@RequestHeader("Authorization") String token) {
        String email = JwtUtil.getEmailFromToken(token);
        return email;
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody(required = false) User user){
        try{
            userService.updateUser(userId, user);
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
