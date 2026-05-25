package com.teamates.service;

import org.springframework.stereotype.Service;

// FLAW: duplicates sendSms logic from NotificationService
// FLAW: no interface, tightly coupled everywhere it's used
@Service
public class SmsNotificationService {

    // FLAW: duplicated method – same as NotificationService.sendSms
    public void sendSms(String recipientId, String message) {
        // TODO: integrate with Twilio or AWS SNS
        System.out.println("[SMS] To: " + recipientId + " | Message: " + message);
    }

    public void sendOrderStatusUpdate(String customerId, String orderNumber, String status) {
        // FLAW: magic string concatenation instead of template
        String message = "Order " + orderNumber + " is now " + status + ".";
        sendSms(customerId, message);
    }

    // FLAW: method not used anywhere — dead code
    public void sendPromoSms(String customerId, String promoCode) {
        String message = "Use promo code " + promoCode + " for 10% off your next order!";
        // FLAW: hardcoded 10% in message, not tied to actual coupon value
        sendSms(customerId, message);
    }
}
