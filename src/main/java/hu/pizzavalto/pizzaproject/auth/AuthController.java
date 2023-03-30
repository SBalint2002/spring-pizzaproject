package hu.pizzavalto.pizzaproject.auth;

import hu.pizzavalto.pizzaproject.dto.UserRegisterDto;
import hu.pizzavalto.pizzaproject.model.Role;
import hu.pizzavalto.pizzaproject.model.User;
import hu.pizzavalto.pizzaproject.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowedHeaders = "*")
@RequestMapping(path = "/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody UserRegisterDto user) {
        try {
            User newUser = User.builder()
                    .first_name(user.getFirst_name())
                    .last_name(user.getLast_name())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(Role.USER)
                    .build();
            authService.addNewUser(newUser);
            return userService.createResponse(newUser);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body("Email is already taken");
        }
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> foundUser = authService.authUser(user.getEmail(), user.getPassword());
        return foundUser.map(userService::createResponse).orElseGet(userService::createErrorResponse);
    }

    @PostMapping(path = "/admin-login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> adminLogin(@RequestBody User user) {
        Optional<User> foundUser = authService.authUser(user.getEmail(), user.getPassword());
        if (foundUser.isPresent()) {
            if (foundUser.get().getRole().equals(Role.ADMIN)) {
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
}
