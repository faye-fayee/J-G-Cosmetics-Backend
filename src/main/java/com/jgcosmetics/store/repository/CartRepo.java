package com.jgcosmetics.store.repository;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepo extends JpaRepository<Cart, Integer> {
    List<Cart> findByUser(User user);
    List<Cart> findBySessionId(String sessionId);
    Cart findByUserAndProductAndShade(User user, Product product, String shade);
    Cart findByProductAndSessionIdAndShade(Product product, String sessionId, String shade);
    Cart deleteById(Long cartId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId")
    void deleteByUser(Long userId);
    @Modifying
    @Query("DELETE FROM Cart c WHERE c.sessionId = :sessionId")
    void deleteBySessionId(String sessionId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId AND c.shade = :shade")
    void deleteByUserIdAndProductIdAndShade(@Param("userId") Long userId, @Param("productId") Long productId, @Param("shade") String shade);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.sessionId = :sessionId AND c.product.id = :productId AND c.shade = :shade")
    void deleteBySessionIdAndProductIdAndShade(@Param("sessionId") String sessionId, @Param("productId") Long productId, @Param("shade") String shade);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.user.id = :userId AND c.product.id = :productId")
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.sessionId = :sessionId AND c.product.id = :productId")
    void deleteBySessionIdAndProductId(@Param("sessionId") String sessionId, @Param("productId") Long productId);

}
