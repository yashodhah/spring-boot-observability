package com.example.observability.order.service;

import com.example.observability.order.model.dto.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OrderService {

    private final OrderProcessingService orderProcessingService;

    public OrderService(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    public void placeOrder(Order order) {
        log.info("Placing order : order id {}", order.id());
        orderProcessingService.processOrder(order);
    }

    public void updateOrderStatus(Order order) {
        log.info("Updating order status : order id {}", order.id());
    }

    public void shipOrder() {
        // Ship order logic
    }

    public void deliverOrder() {
        // Deliver order logic
    }

    public void cancelOrder() {
        // Cancel order logic
    }
}
