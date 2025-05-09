package com.jgcosmetics.store.service;

import com.jgcosmetics.store.dto.CartItemSyncRequest;
import com.jgcosmetics.store.dto.RemoveCartItemRequest;
import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.Product;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.repository.CartRepo;
import com.jgcosmetics.store.repository.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    CartRepo cartRepo;
    @Autowired
    ProductRepo productRepo;
    @Autowired
    private UserService userService;

    // Get cart items for logged in user
    public List<Cart> getCartByUser(User user){
        return cartRepo.findByUser(user);
    }

    // Get cart items for guest user (session)
    public List<Cart> getCartBySession(String sessionId){
        return cartRepo.findBySessionId(sessionId);
    }

    public Cart addToCart(User user, Long productId, int quantity, String sessionId, String shade) {
        // Validate product exists
        Product product = productRepo.findById(productId).orElseThrow(() ->
                new RuntimeException("Product not found with id " + productId)
        );

        // Check if the cart already contains this product for the user/guest
        Cart existingCart = findExistingCart(user, sessionId, product, shade);

        if (existingCart != null) {
            // If exists, update quantity and total price
            existingCart.setQuantity(quantity);
            existingCart.setTotalPrice(existingCart.getPrice() * quantity);
            return cartRepo.save(existingCart);
        } else {
            // If it's a new item, create a new Cart object
            Cart newCart = createNewCart(user, sessionId, product, shade, quantity);
            return cartRepo.save(newCart);
        }
    }

    private Cart findExistingCart(User user, String sessionId, Product product, String shade) {
        if (user != null) {
            return cartRepo.findByUserAndProductAndShade(user, product, shade);
        } else {
            return cartRepo.findByProductAndSessionIdAndShade(product, sessionId, shade);
        }
    }

    private Cart createNewCart(User user, String sessionId, Product product, String shade, int quantity) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setProductName(product.getName());
        cart.setQuantity(quantity);
        cart.setPrice(product.getPrice());
        cart.setTotalPrice(product.getPrice() * quantity);
        cart.setSessionId(sessionId);
        cart.setShade(shade);
        return cart;
    }

    // Sync Cart Items
    public void syncCartItems(List<CartItemSyncRequest> cartItems) {
        for (CartItemSyncRequest item : cartItems) {
            processCartSyncItem(item);
        }
    }

    private void processCartSyncItem(CartItemSyncRequest item) {
        Long productId = item.getProductId();
        Integer quantity = item.getQuantity();
        String sessionId = item.getSessionId();
        Long userId = item.getUserId();
        String shade = item.getShade();

        if (isValidSyncRequest(productId, quantity, sessionId)) {
            User user = (userId != null) ? userService.findById(userId).orElse(null) : null;

            if (quantity == 0) {
                removeItemFromCart(user, sessionId, productId, shade);
            } else {
                addToCart(user, productId, quantity, sessionId, shade);
            }
        }
    }

    private boolean isValidSyncRequest(Long productId, Integer quantity, String sessionId) {
        return productId != null && quantity != null && quantity > 0 && sessionId != null;
    }

    private void removeItemFromCart(User user, String sessionId, Long productId, String shade) {
        if (user != null) {
            removeFromCartByUserProductAndShade(user.getId(), productId, shade);
        } else {
            removeFromCartBySessionProductAndShade(sessionId, productId, shade);
        }
    }

    public void removeCartItemByRequest(RemoveCartItemRequest request) {
        Long userId = request.getUserId();
        String sessionId = request.getSessionId();
        Long productId = request.getProductId();

        if (productId == null) {
            throw new IllegalArgumentException("Error: productId is required.");
        }

        if (userId != null) {
            removeFromCartByUserAndProduct(userId, productId);
        } else if (sessionId != null) {
            removeFromCartBySessionAndProduct(sessionId, productId);
        } else {
            throw new IllegalArgumentException("Error: Either userId or sessionId is required.");
        }
    }


    @Transactional
    public void removeFromCartByUserProductAndShade(Long userId, Long productId, String shade) {
        cartRepo.deleteByUserIdAndProductIdAndShade(userId, productId, shade);
    }

    @Transactional
    public void removeFromCartBySessionProductAndShade(String sessionId, Long productId, String shade) {
        cartRepo.deleteBySessionIdAndProductIdAndShade(sessionId, productId, shade);
    }

    // Remove item from cart by cart ID
    public void removeFromCart(Long cartId) {
        cartRepo.deleteById(cartId);
    }

    // Clear cart for logged-in user
    @Transactional
    public void clearCart(Long userId) {
        cartRepo.deleteByUser(userId);
    }

    // Clear cart for guest
    @Transactional
    public void clearCartBySession(String sessionId) {
        cartRepo.deleteBySessionId(sessionId);
    }

    // Remove item from cart by Product ID (logged-in user)
    @Transactional
    public void removeFromCartByUserAndProduct(Long userId, Long productId) {
        cartRepo.deleteByUserIdAndProductId(userId, productId);
    }

    // Remove item from cart by Product ID (guest)
    @Transactional
    public void removeFromCartBySessionAndProduct(String sessionId, Long productId) {
        cartRepo.deleteBySessionIdAndProductId(sessionId, productId);
    }
}


