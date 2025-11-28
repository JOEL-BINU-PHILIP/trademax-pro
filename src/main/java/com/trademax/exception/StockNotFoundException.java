package com.trademax.exception;

@SuppressWarnings("serial")
public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(String message) { super(message); }
}
