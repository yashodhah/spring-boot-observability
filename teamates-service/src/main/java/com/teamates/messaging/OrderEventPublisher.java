package com.teamates.messaging;

import com.teamates.model.Order;

// FLAW: concrete class, not implementing EventPublisher<OrderEvent> – breaks the interface contract
// FLAW: event is published synchronously inside a @Transactional context
public class OrderEventPublisher {

    // FLAW: no @Autowired or Spring injection – this class is not a Spring bean
    // FLAW: no actual AWS SQS or SNS integration despite AWSConfiguration existing
    public void publishOrderCreated(Order order) {
        System.out.println("Publishing ORDER_CREATED event for order: " + order.getOrderNumber());
        // TODO: send to SQS/SNS
    }

    public void publishOrderCancelled(Order order) {
        System.out.println("Publishing ORDER_CANCELLED event for order: " + order.getOrderNumber());
    }
}
