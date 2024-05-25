package com.example.observability.payment.service;

import com.example.observability.payment.model.OrderPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    public void processPayment(OrderPayment orderPayment) {
        log.info("Processing payment: {}", orderPayment.id());
    }
}
