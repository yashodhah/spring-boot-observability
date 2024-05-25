package com.example.observability.order.model;

public record OrderPayment(Long id,
                           String paymentToken,
                           double amount) {
}
