package com.example.observability.payment;

import org.apache.camel.observation.starter.CamelObservation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@CamelObservation
public class PaymentService {
    public static void main(String[] args) {
        SpringApplication.run(PaymentService.class, args);
    }
}

