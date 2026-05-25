package com.teamates.model;

// FLAW: enum values don't match the String stored in Coupon.type (PERCENTAGE vs "PERCENT")
public enum CouponType {
    PERCENTAGE,
    FIXED_AMOUNT,
    BUY_ONE_GET_ONE,
    FREE_SHIPPING  // FLAW: shipping is not part of this service's domain
}
