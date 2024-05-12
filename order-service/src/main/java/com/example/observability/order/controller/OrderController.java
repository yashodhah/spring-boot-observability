package com.example.observability.order.controller;

import com.example.observability.order.dto.Order;
import com.example.observability.order.model.OrderStatus;
import com.example.observability.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public List<Order> findAll() {
        return List.of(new Order(1L, 1L, List.of(), OrderStatus.PLACED.toString()));
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestBody Order order) {
        orderService.placeOrder(order);
        return "Order has been placed";
    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestBody Order order) {
        orderService.updateOrderStatus(order);
        return "Order has been updated";
    }

}
