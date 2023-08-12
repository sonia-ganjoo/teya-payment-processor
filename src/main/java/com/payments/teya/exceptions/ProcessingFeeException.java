package com.payments.teya.exceptions;

public class ProcessingFeeException extends  Exception {
    public ProcessingFeeException(String errorMessage) {
        super(errorMessage);
    }
}
