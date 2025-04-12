package com.jgcosmetics.store.repository;

import com.jgcosmetics.store.model.Address;
import com.jgcosmetics.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
