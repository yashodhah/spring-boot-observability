package com.example.observability.order.model;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDAO(Long orderId, Long customerId, List<OrderItemDao> orderItemDaos, OrderStatus status, LocalDateTime createdDate, LocalDateTime updatedDate) {
}
