package com.teamates.model;

import jakarta.persistence.*;
import lombok.Data;

// FLAW: Customer entity duplicates fields that could be fetched from an identity service
// FLAW: @Data on entity
// FLAW: password stored in plain text (critical security flaw)
@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;  // FLAW: duplicate ID – both `id` and `customerId` serve as identifiers

    private String firstName;
    private String lastName;

    private String email;  // FLAW: no @Column(unique=true), no email format validation

    private String phone;  // FLAW: no format validation

    private String password;  // FLAW: plain text password storage

    private String address;   // FLAW: address should be a separate embedded object

    private String loyaltyPoints; // FLAW: String instead of int

    private boolean active = true;
}
