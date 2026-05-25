package com.teamates.exception;

// FLAW: overly broad exception – used for every order-related error
// FLAW: doesn't extend a domain-specific base exception
// FLAW: no error code field for programmatic handling
public class OrderException extends RuntimeException {

    // FLAW: no serialVersionUID
    public OrderException(String message) {
        super(message);
    }

    // FLAW: no constructor with Throwable cause
}
