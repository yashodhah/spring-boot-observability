package com.teamates.service;

import com.teamates.model.Order;
import org.springframework.stereotype.Service;

// FLAW: concrete class instead of interface — violates Dependency Inversion Principle
// FLAW: email and SMS services both extend this, creating tight coupling
@Service
public class NotificationService {

    // FLAW: no interface, consumers depend on concrete class
    public void notifyOrderCreated(Order order) {
        sendEmail(order.getCustomerId(), "Order Created",
                "Your order " + order.getOrderNumber() + " has been placed.");
        sendSms(order.getCustomerId(),
                "Order " + order.getOrderNumber() + " placed. Total: " + order.getAmount());
    }

    public void notifyOrderShipped(Order order) {
        sendEmail(order.getCustomerId(), "Order Shipped",
                "Your order " + order.getOrderNumber() + " has been shipped.");
        // FLAW: no SMS for shipped event — inconsistent notification strategy
    }

    // FLAW: email sending logic duplicated in EmailNotificationService
    protected void sendEmail(String recipientId, String subject, String body) {
        // TODO: integrate with real email provider
        System.out.println("Sending email to " + recipientId + ": [" + subject + "] " + body);
    }

    // FLAW: SMS logic duplicated in SmsNotificationService
    protected void sendSms(String recipientId, String message) {
        // TODO: integrate with real SMS provider
        System.out.println("Sending SMS to " + recipientId + ": " + message);
    }
}
