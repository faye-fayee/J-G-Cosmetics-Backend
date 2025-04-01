package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.CartRepo;
import com.jgcosmetics.store.repository.ProductRepo;
import jakarta.transaction.Transactional;
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
    public Cart addToCart(User user, Long productId, int quantity, String sessionId) {
        Product product = productRepo.findById(productId).orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        Cart existingCart;
        if (user != null) {
            existingCart = cartRepo.findByUserAndProductAndSessionId(user, product, sessionId);
        } else {
            existingCart = cartRepo.findByProductAndSessionId(product, sessionId); // For guest users
        }

        if (existingCart != null) {
            // Product already in cart, update the quantity and total price
            existingCart.setQuantity(existingCart.getQuantity() + quantity);
            existingCart.setTotalPrice(existingCart.getPrice() * existingCart.getQuantity());
            return cartRepo.save(existingCart); // Update cart item
        } else {
            // Create a new cart item if product does not exist in cart
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setProduct(product);
            cart.setProductName(product.getName());
            cart.setQuantity(quantity);
            cart.setPrice(product.getPrice());
            cart.setTotalPrice(product.getPrice() * quantity);
            cart.setSessionId(sessionId);

            return cartRepo.save(cart); // Save new cart item
        }
    }


    // Remove item from cart by cart ID
    public void removeFromCart(Long cartId){
        cartRepo.deleteById(cartId);
    }

    // Remove item from cart by Product ID ( if logged-in )
    @Transactional
    public void removeFromCartByUserAndProduct(Long userId, Long productId) {
        cartRepo.deleteByUserIdAndProductId(userId, productId);
    }

    // Remove item from cart by Product ID ( if guest )
    @Transactional
    public void removeFromCartBySessionAndProduct(String sessionId, Long productId) {
        cartRepo.deleteBySessionIdAndProductId(sessionId, productId);
    }


    // Clear cart for logged-in user after checkout
    @Transactional
    public void clearCart(Long userId){
        cartRepo.deleteByUser(userId);
    }

    // Clear cart for guest after checkout
    @Transactional
    public void clearCartBySession(String sessionId){
        cartRepo.deleteBySessionId(sessionId);
    }
}
