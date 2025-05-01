package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.UserRepo;
import com.jgcosmetics.store.service.AddressService;
import com.jgcosmetics.store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AddressService addressService;

    // Register user
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();
        try {
            userService.registerUser(user);
            response.put("message", "User registered successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> loginUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        boolean isValid = userService.isValid(username, password);
        Map<String, Object> response = new HashMap<>();

        if (isValid) {
            Optional<User> optionalUser = userService.findByUsername(username);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                Map<String, Object> userData = new HashMap<>();
                userData.put("id", user.getId());
                userData.put("name", user.getName());
                userData.put("username", user.getUsername());

                response.put("success", true);
                response.put("message", "User logged in successfully");
                response.put("user", userData);
            } else {
                response.put("success", false);
                response.put("message", "User not found");
            }
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");
        }

        return ResponseEntity.ok(response);
    }

    // Get user by username
    @GetMapping("/username/{username}")
    public Optional<User> findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    // Get user details + addresses for checkout
    @GetMapping("/{id}/info")
    public ResponseEntity<?> getUserInfoWithAddresses(@PathVariable Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
        }

        User user = userOptional.get();
        List<Address> addresses = addressService.getAddressesByUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("name", user.getName());
        response.put("username", user.getUsername());
        response.put("addresses", addresses);

        return ResponseEntity.ok(response);
    }

    // Change user password
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String newPassword = payload.get("newPassword");

        Map<String, Object> response = new HashMap<>();

        if(userService.resetPassword(username, newPassword)) {
            response.put("success", true);
            response.put("message", "Password changed successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
