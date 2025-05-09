package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.UserRepo;
import com.jgcosmetics.store.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/account/addresses/")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private UserRepo userRepo;

    private User getCurrentUser() {
        return userRepo.findById(1L).orElseThrow();
    }

    // Get all addresses
    @GetMapping("/{id}")
    public ResponseEntity<List<Address>> getAllAddresses(@PathVariable Long id) {
        Optional<User> userOptional = userRepo.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<Address> addresses = addressService.getAddressesByUser(user);
        return ResponseEntity.ok(addresses);
    }

    // Get an Address by the UserId and Label
    @GetMapping("/{userId}/{label}")
    public Address getAddressByLabel(@PathVariable Long userId, @PathVariable String label) {
        return addressService.getAddressByUserAndLabel(userId, label);
    }

    // Add a new address
    @PostMapping("/{id}")
    public Address addAddress(@RequestBody Address address, @PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        return addressService.addAddress(user, address);
    }


    // Update address
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAddress(
            @PathVariable Long id,
            @RequestBody Address updatedAddress) {

        Address updated = addressService.updateAddress(id, updatedAddress);

        if (updated == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Address update failed.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Address updated successfully.");
        response.put("address", updated);

        return ResponseEntity.ok(response);
    }

    // Delete an address
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
