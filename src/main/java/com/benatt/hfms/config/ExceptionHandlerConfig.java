package com.benatt.hfms.config;

import com.benatt.hfms.data.logs.dtos.Result;
import com.benatt.hfms.exceptions.BadRequestException;
import com.benatt.hfms.exceptions.EmptyResultException;
import com.benatt.hfms.exceptions.InvalidFieldException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;

@ControllerAdvice
public class ExceptionHandlerConfig {
    @Autowired
    private Logger logger;

    @ExceptionHandler(InvalidFieldException.class)
    public ResponseEntity<Result> handleInvalidFieldException(Throwable throwable) {
        return new ResponseEntity<>(new Result(throwable.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Result> handleBadRequestException(Throwable throwable) {
        return new ResponseEntity<>(new Result(throwable.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Result> handleInvalidParameterException(Throwable throwable) {
        return new ResponseEntity<>(new Result(throwable.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyResultException.class)
    public ResponseEntity<Result> handleEmptyResultException(Throwable throwable) {
        return new ResponseEntity<>(new Result(throwable.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<Result> handleSqlException(Throwable throwable) {
        String message = "Please check the request parameters then try again.";
        logger.error(message, throwable);
        return new ResponseEntity<>(new Result(message), HttpStatus.BAD_REQUEST);
    }
}
