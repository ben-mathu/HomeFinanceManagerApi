package com.benatt.hfms.config;

import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;

@ControllerAdvice
public class ExceptionHandlerConfig {
    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<Throwable> handleInvalidFieldException(Throwable throwable) {
        return new ResponseEntity<>(throwable, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Throwable> handleInvalidParameterException(Throwable throwable) {
        return new ResponseEntity<>(throwable, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyResultException.class)
    public ResponseEntity<Throwable> handleEmptyResultException(Throwable throwable) {
        return new ResponseEntity<>(throwable, HttpStatus.BAD_REQUEST);
    }
}
