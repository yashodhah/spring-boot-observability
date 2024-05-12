package com.example.observability.payment.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    public void processPayment(String orderId) {
        log.info("Processing payment: {}", orderId);
    }
}
