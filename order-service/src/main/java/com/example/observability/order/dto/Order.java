package com.example.observability.order.dto;

import com.example.observability.order.model.OrderItemDao;

import java.util.List;

public record Order(Long id, Long customerId, List<OrderItemDao> items, String status) {
}
