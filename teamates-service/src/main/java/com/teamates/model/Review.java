package com.teamates.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// FLAW: Using @Data on a JPA entity (equals/hashCode on mutable entity causes issues)
// FLAW: No validation annotations
// FLAW: rating stored as String instead of int/enum
@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;  // FLAW: public field

    // FLAW: no @Column constraints, nullable by default
    private String customerId;

    private Long productId;  // FLAW: should be @ManyToOne relationship

    private String rating;   // FLAW: rating as String, should be numeric

    private String comment;

    // FLAW: no @Column(nullable = false)
    private LocalDateTime createdAt;

    private String status;   // FLAW: should be an enum, not a raw String

    // FLAW: no audit fields, no updatedAt
}
