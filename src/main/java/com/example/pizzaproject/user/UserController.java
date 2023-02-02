package com.example.pizzaproject.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerNewStudent(@RequestBody User user) {
        userService.addNewUser(user);
        return new ResponseEntity<>("User created successfully", HttpStatus.OK);
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<String> updateUser(
            @PathVariable("userId") Long studentId,
            @RequestParam(required = false) String first_name,
            @RequestParam(required = false) String last_name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(name = "admin", required = false) boolean admin,
            @RequestParam(name = "cook", required = false) boolean cook){
        userService.updateUser(studentId, first_name, last_name, email, password, admin, cook);
        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }
}
