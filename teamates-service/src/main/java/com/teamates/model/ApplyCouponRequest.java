package com.teamates.model;

import lombok.Data;
import java.math.BigDecimal;

// FLAW: Not an entity, but confusingly named like a DB model
// FLAW: mixes request data with response data
@Data
public class ApplyCouponRequest {

    private String couponCode;
    private BigDecimal orderAmount;
    private String customerId;
    // FLAW: request object carries computed result
    private BigDecimal discountedAmount;  // should be in response, not request
    private String message;               // same problem
}
