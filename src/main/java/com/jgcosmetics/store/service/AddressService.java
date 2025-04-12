package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.AddressRepo;
import com.jgcosmetics.store.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Add an Address by User
    public Address addAddress(User user, Address address) {
        address.setUser(user);
        return addressRepo.save(address);
    }


    // Update address by user
    public Address updateAddress(Long id, Address updatedAddress) {
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
