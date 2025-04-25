package com.jgcosmetics.store.controller;

import com.jgcosmetics.store.model.*;
import com.jgcosmetics.store.repository.AddressRepo;
import com.jgcosmetics.store.repository.OrderRepo;
import com.jgcosmetics.store.repository.ProductRepo;
import com.jgcosmetics.store.repository.UserRepo;
import com.jgcosmetics.store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/checkout")
public class OrderController {

    @Autowired private UserRepo userRepo;
    @Autowired private AddressRepo addressRepo;
    @Autowired private ProductRepo productRepo;
    @Autowired private OrderRepo orderRepo;
    @Autowired private OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody Map<String, Object> request) {

        try {
            Long userId = request.get("userId") != null ? Long.valueOf(request.get("userId").toString()) : null;
            Long addressId = request.get("addressId") != null ? Long.valueOf(request.get("addressId").toString()) : null;
            String sessionId = (String) request.get("sessionId");

            String guestFullName = (String) request.get("guestFullName");
            String guestPhone = (String) request.get("guestPhone");
            String guestAddressLine = (String) request.get("guestAddressLine");
            String guestCity = (String) request.get("guestCity");
            String guestPostalCode = (String) request.get("guestPostalCode");
            String guestCountry = (String) request.get("guestCountry");

            // Check if user is logged in
            if (userId != null && addressId != null) {
                // Logged-in user: Place order with userId and addressId
                Order order = orderService.placeOrder(userId, addressId, null, null, null, null, null, null, null);
                return ResponseEntity.ok(order);
            }
            // Handle guest users (no userId)
            else if (sessionId != null) {
                if (guestFullName == null || guestPhone == null || guestAddressLine == null || guestCity == null || guestPostalCode == null || guestCountry == null) {
                    return ResponseEntity.badRequest().body(null);  // Missing guest parameters
                }

                // Place order using sessionId (for guests)
                Order order = orderService.placeOrder(
                        null, addressId, sessionId,
                        guestFullName, guestPhone, guestAddressLine,
                        guestCity, guestPostalCode, guestCountry);
                return ResponseEntity.ok(order);
            } else {
                return ResponseEntity.badRequest().body(null);  // Invalid request, missing parameters
            }

        } catch (Exception e) {
            System.err.println("Error placing order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(null);
        }
    }
}
