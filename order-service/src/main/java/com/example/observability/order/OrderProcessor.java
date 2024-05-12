package com.example.observability.order;

import com.example.observability.order.model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = (Order) exchange.getIn().getBody();
        // validate order
        if (order == null) {
            throw new IllegalArgumentException("Order is required");
        }
    }
}
