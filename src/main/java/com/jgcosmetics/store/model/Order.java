package com.jgcosmetics.store.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean guest;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    private String guestFullName;
    private String guestPhone;
    private String guestAddressLine;
    private String guestCity;
    private String guestPostalCode;
    private String guestCountry;

    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : "No user") +  // Null check for user
                ", address=" + (address != null ? address.getId() : "No address") +  // Null check for address
                ", orderItemsCount=" + (items != null ? items.size() : 0) +  // Null check for items
                '}';
    }


}
