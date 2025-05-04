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

    public boolean isValid(String username, String password) {
        return userRepo.findByUsername(username)
                .map(user -> encoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    // Register new user
    public User registerUser(User user){
        if (userRepo.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Username is already in use");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Find user by ID
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    // Validate user login
    public boolean validateUser(String username, String rawPassword) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.isPresent() && encoder.matches(rawPassword, user.get().getPassword());
    }

    // Reset user password
    public boolean resetPassword(String username, String newPassword) {
        Optional<User> optionalUser = userRepo.findByUsername(username);

        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setPassword(encoder.encode(newPassword));
            userRepo.save(user);
            return true;
        }
        return false;
    }
}
