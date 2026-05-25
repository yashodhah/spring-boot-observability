package com.teamates.controller;

import com.teamates.model.ApplyCouponRequest;
import com.teamates.model.Coupon;
import com.teamates.repository.CouponRepository;
import com.teamates.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLAW: controller directly accesses CouponRepository AND CouponService (mixed abstraction levels)
// FLAW: no error handling – exceptions bubble up as 500s
@RestController
@RequestMapping("api/v1/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;  // FLAW: direct repo access in controller

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        // FLAW: no @Valid, entity passed directly as request body
        return ResponseEntity.ok(couponService.createCoupon(coupon));
    }

    @PostMapping("/apply")
    public ResponseEntity<ApplyCouponRequest> applyCoupon(@RequestBody ApplyCouponRequest request) {
        return ResponseEntity.ok(couponService.applyCoupon(request));
    }

    // FLAW: returns all coupons including inactive/expired ones – data leak
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        return ResponseEntity.ok(couponRepository.findAll());  // FLAW: direct repo access
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateCoupon(@PathVariable Long id) {
        couponService.deactivateCoupon(id);
        return ResponseEntity.noContent().build();
    }

    // FLAW: GET endpoint that modifies state
    @GetMapping("/validate/{code}")
    public ResponseEntity<Coupon> validateCoupon(@PathVariable String code) {
        // FLAW: this is a GET that could trigger side-effects if extended
        return couponRepository.findByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
