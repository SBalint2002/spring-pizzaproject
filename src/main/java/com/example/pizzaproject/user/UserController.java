package com.example.pizzaproject.user;

import com.example.pizzaproject.auth.JwtResponse;
import com.example.pizzaproject.auth.AccessUtil;
import com.example.pizzaproject.auth.RefreshRequest;
import com.example.pizzaproject.auth.RefreshUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserRegisterModel user) {
        try {
            User newUser = User.builder()
                    .first_name(user.getFirst_name())
                    .last_name(user.getLast_name())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(Role.USER)
                    .build();
            userService.addNewUser(newUser);
            return userService.createResponse(newUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Email is already taken");
        }
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> foundUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        return foundUser.map(userService::createResponse).orElseGet(userService::createErrorResponse);
    }

    @PostMapping(path = "/admin-login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> adminLogin(@RequestBody User user) {
        Optional<User> foundUser = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            if (foundUser.get().getRole().equals(Role.ADMIN)) { // Check if user is an admin
                return userService.createResponse(foundUser.get());
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest request) {
        if (RefreshUtil.isExpired(request.getRefreshToken())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String email = RefreshUtil.getEmailFromRefreshToken(request.getRefreshToken());
        User user = userService.findUserByEmail(email).orElse(null);
        if (user != null) {
            JwtResponse response = new JwtResponse(AccessUtil.createJWT(user), request.getRefreshToken());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            JwtResponse response = new JwtResponse(null, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }


    }

    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> getUserData(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        String email = AccessUtil.getEmailFromJWTToken(token);
        Optional<User> user = userService.findUserByEmail(email);
        return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(
                value.getId(),
                value.getFirst_name(),
                value.getLast_name(),
                value.getEmail(),
                value.getRole()))).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));
    }

    @GetMapping(path = "/get-all")
    public ResponseEntity<?> getUsers(@RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)) {
            List<User> users = userService.getUsers();
            List<UserResponseDto> response = users.stream()
                    .map(user -> new UserResponseDto(user.getId(), user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getRole()))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
    }

    @DeleteMapping(path = "{userId}")
    public ResponseEntity<?> deleteUser(
            @PathVariable("userId") Long userId,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        if (AccessUtil.isAdminFromJWTToken(token)) {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You must be an admin to access this resource");
    }

    @PutMapping(path = "{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody(required = false) User user,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.substring(7);
        if (AccessUtil.isExpired(token)) {
            //status code 451
            return ResponseEntity.status(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS).body(null);
        }
        //Only admin
        if (AccessUtil.isAdminFromJWTToken(token)) {
            try {
                userService.updateUserAdmin(userId, user);
                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        //If not admin
        if (userId.equals(userService.getUserIdFromToken(token)) && !AccessUtil.isAdminFromJWTToken(token)) {
            try {
                userService.updateUser(userId, user);
                return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("The user must be admin or can modify only its own information.");
    }
}
