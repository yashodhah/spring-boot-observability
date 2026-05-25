package com.teamates.exception;

// FLAW: separate exception class for every domain entity (redundant, should use a base class with error codes)
public class ReviewException extends RuntimeException {

    public ReviewException(String message) {
        super(message);
    }
}
