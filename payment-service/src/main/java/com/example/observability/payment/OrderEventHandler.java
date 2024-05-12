package com.example.observability.payment;

import com.example.observability.payment.dto.Order;
import com.example.observability.payment.dto.OrderStatus;
import com.example.observability.payment.service.OrderUpdateService;
import com.example.observability.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventHandler {
    private final PaymentService paymentService;

    private final OrderUpdateService orderUpdateService;

    public OrderEventHandler(PaymentService paymentService, OrderUpdateService orderUpdateService) {
        this.paymentService = paymentService;
        this.orderUpdateService = orderUpdateService;
    }

    @KafkaListener(topics = "order-events")
    public void listen(String message) {
        log.info("Received order event: {}", message);
        paymentService.processPayment(message);
        // TODO:
        Order order = new Order(Long.parseLong(message), null, null, OrderStatus.PROCESSING.toString());
        orderUpdateService.updateOrderStatus(order);
    }
}
