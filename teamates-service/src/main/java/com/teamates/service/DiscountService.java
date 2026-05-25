package com.teamates.service;

import com.teamates.model.Discount;
import com.teamates.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// FLAW: @Autowired field injection
// FLAW: logic duplicated from DiscountCalculationService
@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;

    public Discount createDiscount(Discount discount) {
        // FLAW: no validation that startDate < endDate
        // FLAW: both discountPercentage and discountAmount can be set simultaneously
        return discountRepository.save(discount);
    }

    // FLAW: loads all discounts to find applicable ones – no category index
    public List<Discount> getDiscountsForCategory(String category) {
        return discountRepository.findByTargetCategory(category);
    }

    // FLAW: duplicates DiscountCalculationService.getActiveDiscounts
    public List<Discount> getActiveDiscounts() {
        LocalDate today = LocalDate.now();
        return discountRepository.findByStartDateBeforeAndEndDateAfterAndEnabledTrue(today, today);
    }

    // FLAW: BigDecimal used incorrectly – percentage is passed as BigDecimal but treated as whole number
    public BigDecimal applyDiscount(BigDecimal price, BigDecimal discountPercentage) {
        // FLAW: no null checks
        return price.subtract(price.multiply(discountPercentage).divide(BigDecimal.valueOf(100)));
    }
}
