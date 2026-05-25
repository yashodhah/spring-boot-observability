package com.teamates.service;

import org.springframework.stereotype.Service;

// FLAW: duplicates sendEmail logic from NotificationService
// FLAW: not connected to NotificationService via interface
@Service
public class EmailNotificationService {

    // FLAW: duplicated method signature and logic
    public void sendEmail(String recipientId, String subject, String body) {
        // TODO: integrate with real email provider (SES, SendGrid, etc.)
        System.out.println("[EMAIL] To: " + recipientId + " | Subject: " + subject + " | Body: " + body);
    }

    public void sendOrderConfirmation(String customerId, String orderNumber, double amount) {
        // FLAW: double for money amount in method signature
        String body = "Thank you for your order #" + orderNumber + ". Total: $" + amount;
        sendEmail(customerId, "Order Confirmation", body);
    }

    public void sendPasswordResetEmail(String customerId, String resetToken) {
        // FLAW: token is logged/printed — security risk
        String body = "Your password reset token is: " + resetToken;
        System.out.println("[DEBUG] Reset token for " + customerId + ": " + resetToken);
        sendEmail(customerId, "Password Reset", body);
    }
}
