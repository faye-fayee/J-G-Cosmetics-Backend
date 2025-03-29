package com.jgcosmetics.store.repository;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User user);
    List<Cart> findBySessionId(String sessionId);

    void deleteByUser(User user);
    void deleteBySessionId(String sessionId);
}
