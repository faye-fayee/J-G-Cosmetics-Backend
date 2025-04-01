package com.jgcosmetics.store.repository;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User user);
    List<Cart> findBySessionId(String sessionId);

    Cart deleteById(Long cartId);

    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.product = :product AND c.sessionId = :sessionId")
    Cart findByUserAndProductAndSessionId(@Param("user") User user, @Param("product") Product product, @Param("sessionId") String sessionId);

    @Query("SELECT c FROM Cart c WHERE c.user IS NULL AND c.product = :product AND c.sessionId = :sessionId")
    Cart findByProductAndSessionId(@Param("product") Product product, @Param("sessionId") String sessionId);


    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteByUser(Long userId);
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.sessionId = :sessionId")
    void deleteBySessionId(String sessionId);


    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.sessionId = :sessionId AND c.product.id = :productId")
    void deleteBySessionIdAndProductId(@Param("sessionId") String sessionId, @Param("productId") Long productId);


}
