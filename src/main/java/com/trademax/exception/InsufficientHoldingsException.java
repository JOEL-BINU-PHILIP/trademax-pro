package com.trademax.exception;

@SuppressWarnings("serial")
public class InsufficientHoldingsException extends RuntimeException {
    public InsufficientHoldingsException(String message) { super(message); }
}
