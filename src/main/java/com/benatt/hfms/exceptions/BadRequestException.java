package com.benatt.hfms.exceptions;

public class BadRequestException extends Throwable {
    public BadRequestException(String message) {
        super(message);
    }
}
