package com.example.observability.payment.dto;

import java.util.List;

public record Order(Long id, Long customerId, List<OrderItem> items, String status) {
}
