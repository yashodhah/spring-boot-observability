package com.example.observability.order;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceRoute extends RouteBuilder {
    @Autowired
    OrderProcessor orderProcessor;

    @Override
    public void configure() throws Exception {
        // turn on json binding and scan for POJO classes in the model package
        restConfiguration().bindingMode(RestBindingMode.json)
                .bindingPackageScan("com.example.observability.order.model");

        rest().openApi().specification("order-service.json").missingOperation("ignore");

        from("direct:placeOrder")
                .process(orderProcessor::process).
                to("kafka:payments?brokers=localhost:9092");
    }

}
