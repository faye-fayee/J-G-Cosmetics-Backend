package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.UserRepo;
import com.jgcosmetics.store.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Address> getAllAddresses(@PathVariable Long id) {
        User user = userRepo.findById(id).orElseThrow();
        return addressService.getAddressesByUser(user);
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
