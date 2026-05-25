package com.teamates.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// FLAW: God object – coupon holds discount logic, applicability rules, AND usage tracking
// FLAW: @Data on entity
// FLAW: mixing business logic fields and persistence fields
@Entity
@Table(name = "coupons")
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;  // FLAW: no @Column(unique=true, nullable=false)

    private String type;  // FLAW: should be CouponType enum

    private BigDecimal discountValue;

    private BigDecimal minimumOrderAmount;  // FLAW: no default value

    private LocalDate expiryDate;

    private int usageLimit;

    private int usedCount;  // FLAW: race condition if not properly managed

    private boolean active;

    // FLAW: applicable product categories stored as comma-separated string instead of a join table
    private String applicableCategories;

    // FLAW: business rule embedded as field
    private boolean stackable;  // can this coupon be combined with other coupons?

    // FLAW: toString will print all fields including sensitive discount info
    @Transient  // FLAW: this should be computed, but stored as transient incorrectly
    private List<String> parsedCategories;

    // FLAW: manual parsing logic inside the model
    public List<String> getParsedCategories() {
        if (applicableCategories == null || applicableCategories.isEmpty()) {
            return List.of();
        }
        return List.of(applicableCategories.split(","));
    }
}
