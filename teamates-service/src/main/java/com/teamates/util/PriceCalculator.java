package com.teamates.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

// FLAW: utility class is instantiable — should have private constructor
// FLAW: magic numbers throughout
public class PriceCalculator {

    // FLAW: no private constructor — this class is meant to be a static utility
    public PriceCalculator() {}

    // FLAW: GST rate is hardcoded as a magic number
    public static BigDecimal addGst(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(1.09));  // FLAW: 9% GST hardcoded
    }

    // FLAW: duplicates DiscountService.applyDiscount
    public static BigDecimal applyDiscount(BigDecimal price, BigDecimal discountPercent) {
        BigDecimal factor = discountPercent.divide(BigDecimal.valueOf(100));
        return price.subtract(price.multiply(factor));
    }

    // FLAW: scale(2) hardcoded without RoundingMode — throws ArithmeticException for repeating decimals
    public static BigDecimal roundPrice(BigDecimal price) {
        return price.setScale(2);  // FLAW: missing RoundingMode
    }

    // FLAW: method name says "withTax" but actually adds shipping too
    public static BigDecimal finalPriceWithTax(BigDecimal price) {
        BigDecimal tax = price.multiply(BigDecimal.valueOf(0.09));  // FLAW: duplicate GST magic number
        BigDecimal shipping = BigDecimal.valueOf(5.00);             // FLAW: flat shipping fee magic number
        return price.add(tax).add(shipping).setScale(2, RoundingMode.HALF_UP);
    }
}
