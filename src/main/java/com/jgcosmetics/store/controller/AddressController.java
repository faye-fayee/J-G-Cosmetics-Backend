package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.UserRepo;
import com.jgcosmetics.store.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    // Add a new address
    @PostMapping("/{id}")
    public Address addAddress(@RequestBody Address address, @PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        return addressService.addAddress(user, address);
    }

    @PutMapping("/{id}")
    public Address updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
//        User user = userRepo.findById(id).orElseThrow();
        return addressService.updateAddress(id, updatedAddress);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
