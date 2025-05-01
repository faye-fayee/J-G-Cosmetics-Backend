package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.AddressRepo;
import com.jgcosmetics.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;
    @Autowired
    private UserRepo userRepo;

    //Get Address List by User
    public List<Address> getAddressesByUser(User user) {
        return addressRepo.findByUser(user);
    }

    // Get Address By UserId and Label
    public Address getAddressByUserAndLabel(Long userId, String label) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return addressRepo.findByUserAndLabel(user, label)
                .orElseThrow(() -> new RuntimeException("Address not found"));
    }

    // Add an Address by User
    public Address addAddress(User user, Address address) {
        address.setUser(user);
        return addressRepo.save(address);
    }


    // Update address by user
    public Address updateAddress(Long id, Address updatedAddress) {

        System.out.println("Phone before update: " + updatedAddress.getPhone());
        Address address = addressRepo.findById(id).orElseThrow();

        address.setLabel(updatedAddress.getLabel());
        address.setCity(updatedAddress.getCity());
        address.setAddressLine(updatedAddress.getAddressLine());
        address.setPostalCode(updatedAddress.getPostalCode());
        address.setCountry(updatedAddress.getCountry());
        address.setFullName(updatedAddress.getFullName());
        address.setPhone(updatedAddress.getPhone());

        return addressRepo.save(address);
    }

    // Delete an address
    public void deleteAddress(Long id) {
        addressRepo.deleteById(id);
    }
}
