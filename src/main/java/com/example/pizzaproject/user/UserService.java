package com.example.pizzaproject.user;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.pizzaproject.auth.JwtResponse;
import com.example.pizzaproject.auth.JwtUtil;
import com.example.pizzaproject.auth.RefreshUtil;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(User user) throws ResponseStatusException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<User> existingUser = userRepository.findUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already taken");
        }
        userRepository.save(user);
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }

    public static ResponseEntity<JwtResponse> createResponse(User user) {
        String jwtToken = JwtUtil.createJWT(user);
        String refreshToken = RefreshUtil.createRefreshToken(user);
        JwtResponse response = new JwtResponse("success",jwtToken, refreshToken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static ResponseEntity<JwtResponse> createErrorResponse() {
        JwtResponse response = new JwtResponse("failure", null, null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalStateException("User with id " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Long userId, User updateUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        if (updateUser.getFirst_name() != null && updateUser.getFirst_name().length() > 0) {
            user.setFirst_name(updateUser.getFirst_name());
        }
        if (updateUser.getLast_name() != null && updateUser.getLast_name().length() > 0) {
            user.setLast_name(updateUser.getLast_name());
        }
        if (updateUser.getEmail() != null && updateUser.getEmail().length() > 0 && !Objects.equals(updateUser.getEmail(), user.getEmail())) {
            Optional<User> userOptional = userRepository.findUserByEmail(updateUser.getEmail());
            if (userOptional.isPresent()) {
                throw new IllegalStateException("Email is taken");
            }
            user.setEmail(updateUser.getEmail());
        }
        if (updateUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
        if (updateUser.isAdmin()) {
            user.setAdmin(true);
        }
        if (!updateUser.isAdmin()) {
            user.setAdmin(false);
        }
    }
}