package com.trademax.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Custom Exceptions

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> onUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<String> onInsufficientFunds(InsufficientFundsException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(InsufficientHoldingsException.class)
    public ResponseEntity<String> onInsufficientHoldings(InsufficientHoldingsException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<String> onStockNotFound(StockNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> onInvalidQty(InvalidQuantityException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }

    //Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(400).body(msg);
    }

    //Generic Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> onGeneric(Exception ex) {
        return ResponseEntity.status(500).body("Internal server error");
    }
    
    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<String> onDuplicateField(DuplicateFieldException ex) {
        return ResponseEntity.status(400).body(ex.getMessage());
    }
    
    @ExceptionHandler(org.springframework.dao.DuplicateKeyException.class)
    public ResponseEntity<String> onDuplicateKey(org.springframework.dao.DuplicateKeyException ex) {

        String msg = ex.getMessage();

        if (msg.contains("email"))
            return ResponseEntity.status(400).body("Email already exists");

        if (msg.contains("pan"))
            return ResponseEntity.status(400).body("PAN already exists");

        return ResponseEntity.status(400).body("Duplicate field value");
    }
}
