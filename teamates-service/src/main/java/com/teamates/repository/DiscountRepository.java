package com.teamates.repository;

import com.teamates.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    // FLAW: no index on targetCategory – slow on large tables
    List<Discount> findByTargetCategory(String category);

    // FLAW: this query will never efficiently filter by both dates without a composite index
    List<Discount> findByStartDateBeforeAndEndDateAfterAndEnabledTrue(LocalDate start, LocalDate end);
}
