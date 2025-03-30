package com.jgcosmetics.store.repository;

import com.jgcosmetics.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    // Select a product by category
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) = LOWER(:category)")
    List<Product> findByCategory(@Param("category")String category);

    // Edit the Description of a Product
    @Modifying
    @Query("UPDATE Product p SET p.description = :newDescription WHERE p.id = :productId")
    void updateProductDescription(@Param("productId") Long productId, @Param("newDescription") String newDescription);

}
