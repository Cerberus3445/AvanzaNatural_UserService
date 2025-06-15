package com.cerberus.userservice.exception;

public class ServiceUnavailableException extends RuntimeException {

    private final static String MESSAGE = "The service is currently unavailable. Please repeat later.";

    public ServiceUnavailableException(){
        super(MESSAGE);
    }
}
