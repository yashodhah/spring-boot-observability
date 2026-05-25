package com.teamates.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

// FLAW: catches all Throwable including Errors (OutOfMemoryError, StackOverflowError)
// FLAW: returns 500 for ALL exceptions instead of specific HTTP codes per exception type
// FLAW: exception details (stack trace info) should not be returned in the body
@RestControllerAdvice
public class GlobalExceptionHandler {

    // FLAW: catches every RuntimeException and returns 400 – too broad
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("error", ex.getMessage());   // FLAW: may leak sensitive info
        body.put("status", 400);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // FLAW: catches all Throwable – including JVM Errors
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, Object>> handleAll(Throwable ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("error", ex.getMessage());   // FLAW: leaks internal error details
        body.put("stackTrace", ex.getStackTrace()[0].toString());  // FLAW: exposes stack trace
        body.put("status", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // FLAW: OrderException and ReviewException are both RuntimeExceptions,
    //       so the generic RuntimeException handler above will always match first —
    //       these handlers are unreachable!
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Map<String, Object>> handleOrderException(OrderException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", 422);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
    }

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<Map<String, Object>> handleReviewException(ReviewException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", ex.getMessage());
        body.put("status", 404);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
