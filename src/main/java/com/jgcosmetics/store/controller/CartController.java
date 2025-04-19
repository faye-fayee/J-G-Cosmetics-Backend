package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.Cart;
import com.jgcosmetics.store.model.User;
import com.jgcosmetics.store.service.CartService;
import com.jgcosmetics.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> addCart(@RequestBody Map<String, Object> payload) {
        try {
            // Safely parse userId
            Object userIdObj = payload.get("userId");
            Long userId = null;
            if (userIdObj instanceof Number) {
                userId = ((Number) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                try {
                    userId = Long.parseLong((String) userIdObj);
                } catch (NumberFormatException e) {
                    userId = null;
                }
            }

            // Safely parse productId
            Object productIdObj = payload.get("productId");
            Long productId = null;
            if (productIdObj instanceof Number) {
                productId = ((Number) productIdObj).longValue();
            } else if (productIdObj instanceof String) {
                try {
                    productId = Long.parseLong((String) productIdObj);
                } catch (NumberFormatException e) {
                    productId = null;
                }
            }

            // Safely parse quantity
            Object quantityObj = payload.get("quantity");
            int quantity = 0;
            if (quantityObj instanceof Number) {
                quantity = ((Number) quantityObj).intValue();
            } else if (quantityObj instanceof String) {
                try {
                    quantity = Integer.parseInt((String) quantityObj);
                } catch (NumberFormatException e) {
                    quantity = 0;
                }
            }

            // Handle Strings as-is
            String sessionId = payload.get("sessionId") != null ? payload.get("sessionId").toString() : null;
            String shade = payload.get("shade") != null ? payload.get("shade").toString() : null;

            // Validate required fields
            if (productId == null || quantity <= 0 || sessionId == null || shade == null || shade.isBlank()) {
                return ResponseEntity.badRequest().body("Error: Invalid data provided. Product, quantity, session ID, and shade are required.");
            }

            // Find user if userId is provided
            User user = null;
            if (userId != null) {
                user = userService.findById(userId).orElse(null);
                if (user == null) {
                    return ResponseEntity.badRequest().body("Error: User not found with id " + userId);
                }
            }

            // Add or update cart
            cartService.addToCart(user, productId, quantity, sessionId, shade);
            return ResponseEntity.ok("Added to cart successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding item to cart: " + e.getMessage());
        }
    }

    // Sync cart from frontend
    @PostMapping("/sync")
    public ResponseEntity<?> syncCart(@RequestBody List<Map<String, Object>> cartItems) {
        try {
            for (Map<String, Object> item : cartItems) {
                Long productId = null;
                int quantity = 0;
                String sessionId = null;
                Long userId = null;
                String shade = null;

                // Safely parse productId, quantity, and userId as Long and Integer
                if (item.get("productId") != null) {
                    try {
                        productId = Long.valueOf(item.get("productId").toString());  // Convert to Long
                    } catch (NumberFormatException e) {
                        // Handle error (e.g., log it)
                        continue;
                    }
                }

                if (item.get("quantity") != null) {
                    try {
                        quantity = Integer.valueOf(item.get("quantity").toString());  // Convert to Integer
                    } catch (NumberFormatException e) {
                        // Handle error (e.g., log it)
                        continue;
                    }
                }

                // Get sessionId and shade as String (no conversion needed)
                sessionId = item.get("sessionId") != null ? (String) item.get("sessionId") : null;
                shade = item.get("shade") != null ? (String) item.get("shade") : null;

                // Parse userId as Long
                if (item.get("userId") != null) {
                    try {
                        userId = Long.valueOf(item.get("userId").toString());  // Convert to Long
                    } catch (NumberFormatException e) {
                        // Handle error (e.g., log it)
                        continue;
                    }
                }

                // Validate essential fields
                if (productId == null || quantity <= 0 || sessionId == null) {
                    continue; // Skip only if essential fields are missing
                }

                User user = null;
                if (userId != null) {
                    user = userService.findById(userId).orElse(null);
                }

                // Update or remove cart items
                if (quantity == 0) {
                    if (user != null) {
                        cartService.removeFromCartByUserProductAndShade(userId, productId, shade); // For logged-in users
                    } else {
                        cartService.removeFromCartBySessionProductAndShade(sessionId, productId, shade); // For guest users
                    }
                } else {
                    cartService.addToCart(user, productId, quantity, sessionId, shade); // Update quantity if greater than 0
                }
            }

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
    public ResponseEntity<?> removeFromCart(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = payload.get("userId") != null ? ((Number) payload.get("userId")).longValue() : null;
            String sessionId = payload.get("sessionId") != null ? (String) payload.get("sessionId") : null;
            Long productId = payload.get("productId") != null ? ((Number) payload.get("productId")).longValue() : null;

            // Validate required fields
            if (productId == null) {
                return ResponseEntity.badRequest().body("Error: productId is required.");
            }

            // Check if userId is provided (logged-in user)
            if (userId != null) {
                cartService.removeFromCartByUserAndProduct(userId, productId);
                return ResponseEntity.ok("Item removed from cart successfully for user: " + userId);
            }

            // Check if sessionId is provided (guest user)
            if (sessionId != null) {
                cartService.removeFromCartBySessionAndProduct(sessionId, productId);
                return ResponseEntity.ok("Item removed from cart successfully for guest.");
            }

            return ResponseEntity.badRequest().body("Error: Either userId or sessionId is required.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error removing item from cart: " + e.getMessage());
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
