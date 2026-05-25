package com.teamates.service;

import com.teamates.model.Coupon;
import com.teamates.model.ApplyCouponRequest;
import com.teamates.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

// FLAW: @Autowired field injection
// FLAW: no @Transactional on usedCount increment (race condition under concurrency)
@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    // FLAW: no @Transactional – usedCount update is not atomic
    public ApplyCouponRequest applyCoupon(ApplyCouponRequest request) {
        Coupon coupon = couponRepository.findByCode(request.getCouponCode())
                .orElseThrow(() -> new RuntimeException("Coupon not found: " + request.getCouponCode()));

        // FLAW: no check on coupon.isActive()
        // FLAW: expiry check uses isAfter instead of isBefore – wrong logic
        if (coupon.getExpiryDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Coupon expired");
        }

        if (coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new RuntimeException("Coupon usage limit reached");
        }

        BigDecimal discount = calculateDiscount(coupon, request.getOrderAmount());
        BigDecimal discounted = request.getOrderAmount().subtract(discount);

        // FLAW: no floor to zero – can result in negative total
        request.setDiscountedAmount(discounted);
        request.setMessage("Coupon applied successfully");

        // FLAW: not transactional – save could fail after business logic ran
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        return request;  // FLAW: returning mutated request object instead of a proper response DTO
    }

    // FLAW: duplicate logic from Discount.calculateDiscount
    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal amount) {
        if ("PERCENTAGE".equals(coupon.getType())) {
            return amount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
        } else if ("FIXED_AMOUNT".equals(coupon.getType())) {
            return coupon.getDiscountValue();
        }
        return BigDecimal.ZERO;  // FLAW: silently ignores unknown coupon types
    }

    public Coupon createCoupon(Coupon coupon) {
        // FLAW: no validation of coupon code uniqueness before save
        // FLAW: no validation that expiryDate is in the future
        return couponRepository.save(coupon);
    }

    // FLAW: deactivating a coupon loads it from DB and sets a flag – should use direct update query
    public void deactivateCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
        coupon.setActive(false);
        couponRepository.save(coupon);
    }
}
