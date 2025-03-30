package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    //Get all Products
    public List<Product>getAllProducts() {
        return productRepo.findAll();
    }

    //Get product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    //Get all products by category
    public List<Product> getProductsByCategory(String category) {
        System.out.println("Searching for category: " + category);
        List<Product> products = productRepo.findByCategory(category);
        System.out.println("Products found: " + products.size());
        return products;
    }

    //Save product
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    //Delete product by ID
    public void deleteProduct(Long id) {
        productRepo.deleteById(id);
    }
}
