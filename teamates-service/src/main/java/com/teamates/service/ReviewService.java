package com.teamates.service;

import com.teamates.model.Review;
import com.teamates.model.ReviewRequest;
import com.teamates.repository.ProductRepository;
import com.teamates.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// FLAW: @Autowired field injection instead of constructor injection
// FLAW: no @Transactional on write methods
// FLAW: no input validation
@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;  // FLAW: cross-service dependency via repository

    // FLAW: no @Transactional
    public Review createReview(ReviewRequest request) {
        // FLAW: no validation that the product exists
        // FLAW: no check that the customer actually ordered the product
        Review review = new Review();
        review.setCustomerId(request.getCustomerId());
        review.setProductId(request.getProductId());
        review.setRating(request.getRating());  // FLAW: no range check (e.g., 1-5)
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setStatus("PENDING");  // FLAW: magic string instead of enum
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsForProduct(Long productId) {
        // FLAW: no pagination
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getReviewsByCustomer(String customerId) {
        return reviewRepository.findByCustomerId(customerId);
    }

    // FLAW: approving a review mutates status using magic string
    public Review approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));  // FLAW: generic RuntimeException
        review.setStatus("APPROVED");  // FLAW: magic string
        return reviewRepository.save(review);
    }

    // FLAW: delete doesn't check ownership (any caller can delete any review)
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // FLAW: average rating computed by loading ALL reviews into memory
    public double getAverageRating(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) return 0;
        return reviews.stream()
                .mapToInt(r -> {
                    try {
                        return Integer.parseInt(r.getRating());  // FLAW: fragile parse
                    } catch (NumberFormatException e) {
                        return 0;  // FLAW: silently ignores bad data
                    }
                })
                .average()
                .orElse(0);
    }
}
