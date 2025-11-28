package com.trademax.exception;

@SuppressWarnings("serial")
public class InvalidQuantityException extends RuntimeException {
	public InvalidQuantityException(String message) {
        super(message);
    }
}
