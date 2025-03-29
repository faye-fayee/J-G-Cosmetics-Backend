package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // Delete a product by ID
    @DeleteMapping("{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "Product with id " + id + " was deleted";
    }
}
