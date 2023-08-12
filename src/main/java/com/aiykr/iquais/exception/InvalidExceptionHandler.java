package com.aiykr.iquais.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidExceptionHandler {
    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<String> handleCustomException(InvalidCredentials invalidCredentials){
        return new ResponseEntity<>(invalidCredentials.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
