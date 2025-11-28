package com.trademax.exception;

@SuppressWarnings("serial")
public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String msg) {
        super(msg);
    }
}