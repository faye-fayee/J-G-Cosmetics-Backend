package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductService productService;

    // Get all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get product by ID
    @GetMapping("/{id}")
    public Optional<Product> getProductById(int productId) {
        return productService.getProductById(productId);
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(String category) {
        return productService.getProductsByCategory(category);
    }

    // Add a new product
    public Product addProduct(Product product) {
        return productService.saveProduct(product);
    }

    // Delete a product by ID
    public String deleteProduct(int productId) {
        productService.deleteProduct(productId);
        return "Product with id " + productId + " was deleted";
    }
}
