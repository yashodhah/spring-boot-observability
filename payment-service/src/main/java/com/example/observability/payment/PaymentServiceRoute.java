package com.example.observability.payment;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("kafka:payment?brokers=localhost:9092")
                .log("Message received : ${body}");
    }
}
