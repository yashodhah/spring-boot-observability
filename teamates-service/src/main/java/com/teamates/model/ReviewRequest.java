package com.teamates.model;

import lombok.Data;

// FLAW: @Data class used as request DTO instead of a record
// FLAW: no input validation annotations
@Data
public class ReviewRequest {

    private String customerId;
    private Long productId;
    private String rating;      // FLAW: String instead of int
    private String comment;
    private String reviewDate;  // FLAW: String instead of LocalDate
}
