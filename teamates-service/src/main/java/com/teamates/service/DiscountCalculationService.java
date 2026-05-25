package com.teamates.service;

import com.teamates.model.Discount;
import com.teamates.model.Order;
import com.teamates.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

// FLAW: mixes static utility methods with Spring-managed state
// FLAW: @Autowired field injection
@Service
public class DiscountCalculationService {

    @Autowired
    private DiscountRepository discountRepository;

    // FLAW: method is an instance method but behaves as static (no state used besides repo)
    public BigDecimal calculateFinalAmount(Order order, String couponCode) {
        BigDecimal originalAmount = order.getAmount();

        // FLAW: loads ALL discounts then filters in memory (N+1 style inefficiency)
        List<Discount> allDiscounts = discountRepository.findAll();
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (Discount discount : allDiscounts) {
            if (discount.isValid()) {
                // FLAW: applies ALL valid discounts, not just applicable ones
                totalDiscount = totalDiscount.add(discount.calculateDiscount(originalAmount));
            }
        }

        // FLAW: no check if totalDiscount exceeds originalAmount
        BigDecimal finalAmount = originalAmount.subtract(totalDiscount);
        // FLAW: magic number – scale hardcoded to 2
        return finalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    // FLAW: static method in a Spring service – breaks the Spring lifecycle
    public static BigDecimal applyPercentageDiscount(BigDecimal amount, int percentage) {
        // FLAW: int percentage, should be BigDecimal; no validation of range
        return amount.subtract(amount.multiply(BigDecimal.valueOf(percentage)).divide(BigDecimal.valueOf(100)));
    }

    // FLAW: duplicated rounding logic (already in calculateFinalAmount)
    public BigDecimal roundToTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public List<Discount> getActiveDiscounts() {
        LocalDate today = LocalDate.now();
        // FLAW: wrong argument order — startDate and endDate swapped
        return discountRepository.findByStartDateBeforeAndEndDateAfterAndEnabledTrue(today, today);
    }
}
