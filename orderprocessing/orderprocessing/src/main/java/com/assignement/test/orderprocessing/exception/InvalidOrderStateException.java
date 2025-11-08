package com.assignement.test.orderprocessing.exception;

public class InvalidOrderStateException extends  RuntimeException{
    public InvalidOrderStateException(String message) {
        super(message);
    }
}
