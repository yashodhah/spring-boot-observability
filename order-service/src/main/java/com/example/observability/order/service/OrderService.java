package com.example.observability.order.service;

import com.example.observability.order.dto.Order;
import com.example.observability.order.model.OrderDAO;
import com.example.observability.order.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderService {

    private final OrderProcessingService orderProcessingService;

    public OrderService(OrderProcessingService orderProcessingService) {
        this.orderProcessingService = orderProcessingService;
    }

    public void placeOrder(Order order) {
        log.info("Placing order");
        orderProcessingService.processOrder(order);
        OrderDAO orderDAO = new OrderDAO(order.id(), order.customerId(), order.items(), OrderStatus.PLACED, LocalDateTime.now(), LocalDateTime.now());
        // persist order to database
    }

    public void updateOrderStatus(Order order) {
        log.info("Updating order status");
        // get order object form the database
        // update order status
        // persist order to database
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
