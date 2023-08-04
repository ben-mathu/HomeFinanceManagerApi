package com.benatt.hfms.config;

import com.benatt.hfms.exceptions.InvalidFieldException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerConfig {
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<Object> handleInvalidFieldException(Throwable throwable) {
        return new ResponseEntity<>(throwable, HttpStatus.BAD_REQUEST);
    }
}
