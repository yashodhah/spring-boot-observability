package com.example.observability.order.service;

import com.example.observability.order.model.dto.Order;
import com.example.observability.order.model.OrderDAO;
import com.example.observability.order.model.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderProcessingService {

    @Autowired
    OrderPaymentService orderPaymentService;

    public void processOrder(Order order) {
        validateOrder(order);
        persistOrder(order);
        orderPaymentService.processOrderPayments(order);
    }

    private void persistOrder(Order order) {
        OrderDAO orderDAO = new OrderDAO(order.id(), order.customerId(), order.items(), OrderStatus.PLACED, LocalDateTime.now(), LocalDateTime.now());
        log.info("Persisting order details for order : order id {}", order.id());
    }

    private void validateOrder(Order order) {
        log.info("validating order details for order {}", order.id());
    }
}
