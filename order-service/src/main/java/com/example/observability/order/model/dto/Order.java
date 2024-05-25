package com.example.observability.order.model.dto;

import com.example.observability.order.model.OrderStatus;

import java.util.List;

public record Order(Long id, Long customerId, List<OrderItem> items, String paymentToken, OrderStatus orderStatus) {
}

