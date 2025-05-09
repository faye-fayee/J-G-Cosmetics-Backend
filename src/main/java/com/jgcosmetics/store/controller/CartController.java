package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.dto.AddCartRequest;
import com.jgcosmetics.store.dto.CartItemSyncRequest;
import com.jgcosmetics.store.dto.RemoveCartItemRequest;
import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.service.CartService;
import com.jgcosmetics.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    // Get cart items by user
    @GetMapping("/user/{userId}")
    public List<Cart> getCartByUser (@PathVariable Long userId) {
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
    public ResponseEntity<?> addCart(@RequestBody AddCartRequest cartRequest) {
        try {
            // Safely handle userId (if present)
            User user = null;
            if (cartRequest.getUserId() != null) {
                user = userService.findById(cartRequest.getUserId()).orElse(null);
                if (user == null) {
                    return ResponseEntity.badRequest().body("Error: User not found with id " + cartRequest.getUserId());
                }
            }

            // Call the service layer to add the item
            cartService.addToCart(user, cartRequest.getProductId(), cartRequest.getQuantity(),
                    cartRequest.getSessionId(), cartRequest.getShade());

            return ResponseEntity.ok("Added to cart successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding item to cart: " + e.getMessage());
        }
    }

    // Sync Cart
    @PostMapping("/sync")
    public ResponseEntity<?> syncCart(@RequestBody List<CartItemSyncRequest> cartItems) {
        try {
            cartService.syncCartItems(cartItems);
            return ResponseEntity.ok("Cart synced successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error syncing cart: " + e.getMessage());
        }
    }

    // Delete item from cart
    @DeleteMapping("/remove/cart-id/{cartId}")
    public String removeFromCart(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return "Removed items from cart successfully";
    }

    // Delete items from cart by product ID
    @DeleteMapping("/remove/cart-item")
    public ResponseEntity<?> removeFromCart(@RequestBody RemoveCartItemRequest request) {
        try {
            cartService.removeCartItemByRequest(request);
            return ResponseEntity.ok("Item removed from cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error removing item from cart: " + e.getMessage());
        }
    }

    // Clear cart for logged-in user
    @DeleteMapping("/clear/user/{userId}")
    public String clearCartByUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        cartService.clearCart(userId);
        return "Cart cleared for user successfully";
    }

    // Clear cart for session ID
    @DeleteMapping("/clear/session/{sessionId}")
    public String clearCartBySession(@PathVariable String sessionId) {
        cartService.clearCartBySession(sessionId);
        return "Cart cleared for session successfully";
    }
}
