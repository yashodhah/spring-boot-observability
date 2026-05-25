package com.teamates.util;

import com.teamates.model.Order;
import com.teamates.model.OrderItem;
import com.teamates.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// FLAW: utility class has Spring state (@Autowired repository) — breaks stateless utility pattern
// FLAW: mixes static helpers with instance methods
@Component
public class OrderUtils {

    @Autowired
    private OrderRepository orderRepository;  // FLAW: state in a utility class

    // FLAW: static method but class is a Spring component — inconsistent
    public static String generateOrderNumber() {
        // FLAW: not unique under high concurrency (timestamp collision)
        return "ORD-" + System.currentTimeMillis();
    }

    // FLAW: duplicate of logic in OrderService.createOrder
    public BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // FLAW: instance method in utility class that calls the DB
    public boolean orderExists(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber).isPresent();
    }

    // FLAW: ignores timezone – uses system default
    public static String formatInstant(Instant instant) {
        return instant.toString();  // FLAW: raw ISO-8601, no locale/timezone formatting
    }

    // FLAW: magic number 30 – business rule baked into utility
    public static boolean isOrderRecent(Order order) {
        return Instant.now().minusSeconds(30 * 24 * 60 * 60L).isBefore(order.getCreatedAt());
    }
}
