package com.shiproutepro.backend.exception;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }
}