package com.example.observability.order.controller;

import com.example.observability.order.model.dto.Order;
import com.example.observability.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        orderService.placeOrder(order);
        return ResponseEntity.ok("Order has been placed");
    }

    @PutMapping
    public ResponseEntity<String> updateOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
        return ResponseEntity.ok("Order has been updated");
    }
}
