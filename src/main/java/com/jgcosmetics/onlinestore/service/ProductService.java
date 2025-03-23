package com.jgcosmetics.onlinestore.service;

import com.jgcosmetics.onlinestore.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private List<Product> productList = new ArrayList<>();

    public List<Product> getAllProducts() {
        return productList;
    }

    public Product getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Product addProduct(Product product) {
        productList.add(product);
        return product; // Return the added product
    }
    

    public void deleteProduct(Long id) {
        productList.removeIf(product -> product.getId().equals(id));
    }

    public Product updateProduct(Long id, Product productDetails) {
        for (Product product : productList) {
            if (product.getId().equals(id)) {
                product.setName(productDetails.getName());
                product.setPrice(productDetails.getPrice());
                product.setDescription(productDetails.getDescription());
                return product; // Return updated product
            }
        }
        return null; // Return null if product is not found
    }
    
}
