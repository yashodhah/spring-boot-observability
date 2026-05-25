package com.teamates.model;

import lombok.Data;

// FLAW: CustomerProfile duplicates Customer fields — both classes represent the same concept
// FLAW: not an entity, but used to shadow the Customer entity causing confusion
@Data
public class CustomerProfile {

    private Long id;
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    // FLAW: no mapping to Customer – these two classes are maintained independently
    private int totalOrders;
    private double totalSpent;  // FLAW: double for money
    private String membershipLevel; // FLAW: should be an enum

    // FLAW: data duplication, should be derived from Customer entity
    private String address;
}
