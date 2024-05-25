package com.example.observability.order.controller;

import com.example.observability.order.model.dto.Order;
import com.example.observability.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place-order")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        orderService.placeOrder(order);
        return ResponseEntity.ok("Order has been placed");
    }

    @PostMapping("/update-order-status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Order order) {
        orderService.updateOrderStatus(order);
        return ResponseEntity.ok("Order has been updated");
    }
}
