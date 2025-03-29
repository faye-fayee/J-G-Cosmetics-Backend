package com.jgcosmetics.store.repository;

import com.jgcosmetics.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(String category);
}
