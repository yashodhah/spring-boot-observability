package com.teamates.messaging;

import com.teamates.model.Review;

// FLAW: not a Spring bean, no interface implementation
// FLAW: duplicated pattern from OrderEventPublisher with no shared abstraction
public class ReviewEventPublisher {

    public void publishReviewSubmitted(Review review) {
        System.out.println("Publishing REVIEW_SUBMITTED for product: " + review.getProductId());
    }

    public void publishReviewApproved(Review review) {
        System.out.println("Publishing REVIEW_APPROVED for review id: " + review.getId());
    }
}
