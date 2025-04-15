package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Register user
    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User user) {
        userService.registerUser(user);
        return "User registered successfully";
    }

    // Login User
    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> loginUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        // Use isValid to check credentials
        boolean isValid = userService.isValid(username, password);

        Map<String, Object> response = new HashMap<>();
        if (isValid) {
            response.put("success", true);
            response.put("message", "User logged in successfully");
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

}
