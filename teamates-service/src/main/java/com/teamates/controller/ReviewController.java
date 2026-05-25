package com.teamates.controller;

import com.teamates.model.Review;
import com.teamates.model.ReviewRequest;
import com.teamates.repository.ReviewRepository;
import com.teamates.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLAW: mixes direct repository access AND service access (bypasses service layer)
// FLAW: business logic (approval workflow) done in controller
// FLAW: @Autowired field injection
@RestController
@RequestMapping("api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;  // FLAW: controller directly accesses repository

    @PostMapping
    public ResponseEntity<Review> submitReview(@RequestBody ReviewRequest request) {
        // FLAW: no @Valid, no input validation
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsForProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsForProduct(productId));
    }

    @GetMapping("/product/{productId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getAverageRating(productId));
    }

    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<Review> approveReview(@PathVariable Long reviewId) {
        // FLAW: no authorization check – anyone can approve reviews
        return ResponseEntity.ok(reviewService.approveReview(reviewId));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        // FLAW: no authorization check – anyone can delete any review
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // FLAW: raw repository access in controller, bypasses service layer
    @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll());
    }

    // FLAW: business logic to reject a review is duplicated here instead of in service
    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<Review> rejectReview(@PathVariable Long reviewId) {
        Review review = reviewRepository.findById(reviewId)  // FLAW: direct repo access
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setStatus("REJECTED");  // FLAW: magic string
        return ResponseEntity.ok(reviewRepository.save(review));  // FLAW: direct repo save
    }
}
