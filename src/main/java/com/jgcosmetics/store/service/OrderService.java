package com.jgcosmetics.store.service;

import com.jgcosmetics.store.model.*;
import com.jgcosmetics.store.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderItemRepo orderItemRepo;
    @Autowired
    private CartRepo cartRepo;

    @Transactional
    public Order placeOrder(Long userId, Long addressId, String sessionId, String guestFullName, String guestPhone, String guestAddressLine, String guestCity, String guestPostalCode, String guestCountry) {
        Address address = null;
        List<Cart> cartItems;
        User user = null;

        if (userId != null) {
            user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            address = addressRepo.findById(addressId)
                    .orElseThrow(() -> new RuntimeException("Address not found"));
            cartItems = cartRepo.findByUser(user);
        } else if (sessionId != null) {
            if (guestFullName == null || guestPhone == null || guestAddressLine == null || guestCity == null || guestPostalCode == null || guestCountry == null) {
                throw new RuntimeException("Guest address information is incomplete");
            }

            address = new Address();
            address.setFullName(guestFullName);
            address.setPhone(guestPhone);
            address.setAddressLine(guestAddressLine);
            address.setCity(guestCity);
            address.setPostalCode(guestPostalCode);
            address.setCountry(guestCountry);
            addressRepo.save(address);

            address.setUser(null);

            cartItems = cartRepo.findBySessionId(sessionId);
        } else {
            throw new RuntimeException("No userId or sessionId provided");
        }

        if (cartItems.isEmpty()) {
            throw new RuntimeException("No items found in cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());

        if (sessionId != null) {
            order.setGuestFullName(guestFullName);
            order.setGuestPhone(guestPhone);
            order.setGuestAddressLine(guestAddressLine);
            order.setGuestCity(guestCity);
            order.setGuestPostalCode(guestPostalCode);
            order.setGuestCountry(guestCountry);
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSelectedShade(cartItem.getShade());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        orderRepo.save(order);
        System.out.println("Guest Cart Items: " + cartItems.size());

        // Clear the cart for the guest session
        if (sessionId != null) {
            for (Cart cartItem : cartItems) {
                // Log cartItem information to ensure that it matches sessionId
                System.out.println("Deleting Cart Item: " + cartItem.getId() + " with sessionId: " + cartItem.getSessionId());
            }

            // Clear the cart explicitly by sessionId
            cartRepo.deleteBySessionId(sessionId);
            System.out.println("Cleared cart for sessionId: " + sessionId);  // Debugging line
        } else {
            cartRepo.deleteAll(cartItems);
            System.out.println("Cleared cart for userId: " + userId);  // Debugging line
        }

        return order;
    }
}
