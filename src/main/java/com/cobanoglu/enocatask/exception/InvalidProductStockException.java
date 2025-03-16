package com.cobanoglu.enocatask.exception;

public class InvalidProductStockException extends RuntimeException {
    public InvalidProductStockException(String message) {
        super(message);
    }
}
