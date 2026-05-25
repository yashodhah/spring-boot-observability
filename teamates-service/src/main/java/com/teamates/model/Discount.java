package com.teamates.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

// FLAW: separate Discount entity that duplicates Coupon fields
// FLAW: mixing concerns – this should just be a value object, not an entity
@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FLAW: same fields as Coupon – duplicate model
    private String discountCode;
    private String discountName;
    private BigDecimal discountPercentage;
    private BigDecimal discountAmount; // FLAW: having both percentage AND amount is ambiguous
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetCategory; // FLAW: duplicates Coupon.applicableCategories
    private boolean enabled;

    // FLAW: business logic in the model
    public boolean isValid() {
        LocalDate today = LocalDate.now();
        return enabled && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    // FLAW: ambiguous method – which takes priority, amount or percentage?
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (discountPercentage != null && discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
            return orderAmount.multiply(discountPercentage).divide(BigDecimal.valueOf(100));
        }
        return discountAmount != null ? discountAmount : BigDecimal.ZERO;
    }
}
