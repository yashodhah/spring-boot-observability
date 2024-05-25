package com.example.observability.order.model;

import com.example.observability.order.model.dto.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDAO(Long orderId, Long customerId, List<OrderItem> orderItems, OrderStatus status, LocalDateTime createdDate, LocalDateTime updatedDate) {
}
