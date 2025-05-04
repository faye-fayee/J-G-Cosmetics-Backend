package com.jgcosmetics.store.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties("user")
@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "address_line")
    private String addressLine;

    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}
