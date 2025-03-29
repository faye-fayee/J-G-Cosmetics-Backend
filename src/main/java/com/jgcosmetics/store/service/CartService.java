package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.CartRepo;
import com.jgcosmetics.store.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;
    @Autowired
    ProductRepo productRepo;

    // Get cart items for logged in user
    public List<Cart> getCartByUser(User user){
        return cartRepo.findByUser(user);
    }

    // Get cart items for guest user (session)
    public List<Cart> getCartBySession(String sessionId){
        return cartRepo.findBySessionId(sessionId);
    }

    // Add items to cart for logged in users or guest
    public Cart addToCart(User user, int productId, int quantity, String sessionId){
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Set Cart Info
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(quantity);
        cart.setPrice(product.getPrice());
        cart.setTotalPrice(product.getPrice() * quantity);
        cart.setSessionId(sessionId);

        // Save cart info
        return cartRepo.save(cart);
    }

    // Remove item from cart
    public void removeFromCart(int cartId){
        cartRepo.deleteById(cartId);
    }

    // Clear cart for logged-in user after checkout
    public void clearCart(User user){
        cartRepo.deleteByUser(user);
    }

    // Clear cart for guest after checkout
    public void clearCartBySession(String sessionId){
        cartRepo.deleteBySessionId(sessionId);
    }
}
