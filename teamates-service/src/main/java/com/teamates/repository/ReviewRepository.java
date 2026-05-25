package com.teamates.repository;

import com.teamates.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// FLAW: business logic embedded in repository via JPQL
// FLAW: raw JPQL string query is fragile and not type-safe
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByCustomerId(String customerId);

    // FLAW: JPQL query selecting only rating but returning Review – misleading
    @Query("SELECT r FROM Review r WHERE r.productId = ?1 AND r.status = 'APPROVED'")
    List<Review> findApprovedReviewsByProduct(Long productId);

    // FLAW: average computed in repository, but rating is a String – this will fail at runtime
    @Query("SELECT AVG(CAST(r.rating AS double)) FROM Review r WHERE r.productId = ?1")
    Double getAverageRatingForProduct(Long productId);
}
