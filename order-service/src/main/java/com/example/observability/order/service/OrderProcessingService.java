package com.example.observability.order.service;

import com.example.observability.order.dto.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void processOrder(Order order) {
        validateOrder(order);
        kafkaTemplate.send("order-events", String.valueOf(order.id()));
    }

    private void validateOrder(Order order) {
        // Validate order logic
    }

    public void processPayments(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
