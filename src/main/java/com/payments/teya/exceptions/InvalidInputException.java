package com.payments.teya.exceptions;

public class InvalidInputException extends  Exception {
    public InvalidInputException(String errorMessage) {
        super(errorMessage);
    }
}
