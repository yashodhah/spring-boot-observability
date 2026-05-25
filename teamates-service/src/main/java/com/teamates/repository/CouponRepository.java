package com.teamates.repository;

import com.teamates.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// FLAW: no custom queries despite needing to check expiry and active status
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    // FLAW: no findByCodeAndActiveTrue or expiry-date filtering
}
