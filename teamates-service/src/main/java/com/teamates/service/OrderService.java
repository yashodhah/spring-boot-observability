package com.teamates.service;

import com.teamates.model.CreateOrderRequest;
import com.teamates.model.Order;
import com.teamates.model.OrderItem;
import com.teamates.repository.OrderRepository;
import com.teamates.util.OrderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class OrderService {

    @Autowired
    private  OrderRepository orderRepository;
    @Autowired
    private OrderValidationService orderValidationService;
    // FLAW: directly coupling OrderService to NotificationService instead of using an event
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Order createOrder(CreateOrderRequest createOrderRequest) {

        ValidatedOrderCreationRequest validated = orderValidationService.validateOrderCreationRequest(createOrderRequest);
        List<OrderItem> orderItems = IntStream.range(0, validated.products().size())
                .mapToObj(i -> OrderItem.builder()
                        .product(validated.products().get(i))
                        .quantity(createOrderRequest.items().get(i).quantity())
                        .price(validated.products().get(i).getPrice())
                        .build())
                .toList();

        // TODO: we need third party payment service to process payment.

        // Create order entity
        Order order = Order.builder()
                .orderNumber("ORD-" + System.currentTimeMillis()) // TODO: Meaningful order number
                .status(Order.OrderStatus.PENDING)
                .createdAt(Instant.now())
                .items(orderItems)
                .storeId(createOrderRequest.storeId())
                .customerId("demo-customer") // Placeholder for customer ID, should be replaced with actual customer ID
                .amount(orderItems.stream()
                        .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)) // Calculate total amount
                .build();

        // Set the parent order to each order item, circular reference here
        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        // Save order & publish event
        Order savedOrder = orderRepository.save(order);
        // FLAW: notification called inside @Transactional – if email fails, transaction might rollback
        notificationService.notifyOrderCreated(savedOrder);
        return savedOrder;
    }

    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("Order not found."));
    }

    public List<Order> getAllOrders() {
        // FLAW: no pagination – loads all orders into memory
        return orderRepository.findAll();
    }

    // FLAW: cancel logic uses magic string status, and doesn't check if order can be cancelled
    public Order cancelOrder(String orderNumber) {
        Order order = getOrderByNumber(orderNumber);
        order.setStatus(Order.OrderStatus.CANCELLED);
        return orderRepository.save(order);  // FLAW: missing @Transactional
    }
}
