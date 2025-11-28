package com.trademax.exception;

public class DuplicateFieldException extends RuntimeException {
    public DuplicateFieldException(String msg) {
        super(msg);
    }
}