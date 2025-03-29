package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.service.CartService;
import com.jgcosmetics.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    // Get cart items by user
    @GetMapping("/user/{userId}")
    public List<Cart> getCartByUser (@PathVariable int userId) {
        User user = new User();
        user.setId(userId);
        return cartService.getCartByUser(user);
    }

    // Get cart items by session ID
    @GetMapping("/session/{sessionId}")
    public List<Cart> getCartBySession(@PathVariable String sessionId) {
        return cartService.getCartBySession(sessionId);
    }

    // Add items to cart
    @PostMapping("/add")
    public String addCart(@RequestParam(required = false) Integer userId,
                          @RequestParam int productId,
                          @RequestParam int quantity,
                          @RequestParam(required = false) String sessionId) {
        User user = null;
        if (userId != null) {
            user = userService.findByUsername(String.valueOf(userId)).orElse(null);
            if (user == null) {
                return "Error: User not found with id " + userId;
            }
        }
        try {
            cartService.addToCart(user, productId, quantity, sessionId);
            return "Added cart successfully";
        } catch (Exception e) {
            return "Error adding item to cart: " + e.getMessage();
        }
    }

    // Delete item from cart
    @DeleteMapping("/remover/{cardId}")
    public String removeFromCart(@RequestParam int cartId) {
        cartService.removeFromCart(cartId);
        return "Removed cart successfully";
    }

    // Clear cart for logged-in user
    @DeleteMapping("/clear/user/{userId}")
    public String clearCartByUser(@PathVariable int userId) {
        User user = new User();
        user.setId(userId);
        cartService.clearCart(user);
        return "Cart cleared for user successfully";
    }

    // Clear cart for session ID
    public String clearCartBySession(@PathVariable String sessionId) {
        cartService.clearCartBySession(sessionId);
        return "Cart cleared for session successfully";
    }
}
