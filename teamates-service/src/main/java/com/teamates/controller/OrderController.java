package com.teamates.controller;

import com.teamates.model.CreateOrderRequest;
import com.teamates.model.Order;
import com.teamates.service.OrderService;
import com.teamates.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// FLAW: OrderController now also handles review-related queries (violates SRP)
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    // FLAW: review concerns injected into order controller
    private final ReviewService reviewService;

    public OrderController(OrderService orderService, ReviewService reviewService) {
        this.orderService = orderService;
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return ResponseEntity.ok(orderService.createOrder(createOrderRequest));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrderByNumber(orderNumber));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // FLAW: cancel uses GET verb instead of DELETE or PATCH
    @GetMapping("/{orderNumber}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.cancelOrder(orderNumber));
    }

    // FLAW: review endpoint on order controller – belongs in ReviewController
    @GetMapping("/{orderId}/reviews")
    public ResponseEntity<?> getReviewsForOrder(@PathVariable Long orderId) {
        // FLAW: orderId is passed as productId – wrong mapping
        return ResponseEntity.ok(reviewService.getReviewsForProduct(orderId));
    }
}
