package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Register new user
    public User registerUser(User user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepo.findByUsername(username));
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepo.findByEmail(email));
    }

    // Validate user login
    public boolean validateUser(String username, String rawPassword) {
        Optional<User> user = findByUsername(username);
        return user.isPresent() && encoder.matches(rawPassword, user.get().getPassword());
    }
}
