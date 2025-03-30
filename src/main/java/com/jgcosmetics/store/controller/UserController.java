package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String loginUser(@RequestBody Map<String, String> payload) {
        String username = payload.get("username");
        String password = payload.get("password");

        // Use isValid to check credentials
        boolean isValid = userService.isValid(username, password);
        return isValid ? "User logged in successfully" : "Invalid username or password";
    }

    // Get user by username
    @GetMapping("/username/{username}")
    public Optional<User> findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public Optional<User> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

}
