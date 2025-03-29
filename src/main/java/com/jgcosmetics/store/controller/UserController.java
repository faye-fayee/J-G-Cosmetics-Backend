package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.service.UserService;
import org.springframework.web.bind.annotation.CrossOrigin;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        boolean isValid = userService.validateUser(username, password);
        return isValid? "User logged in successfully" : "Invalid username or password";
    }

    // Get user by username
    @GetMapping("/{username}")
    public Optional<User> findByUsername(String username) {
        return userService.findByUsername(username);
    }

    // Get user by email
    @GetMapping("/{email}")
    public Optional<User> findByEmail(String email) {
        return userService.findByEmail(email);
    }

}
