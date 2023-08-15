package com.aiykr.iquais.exception;

import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IquaisException.class)
    public ResponseEntity<Response> handleIquaisException(IquaisException iqException) {
        Response response = new Response();
        response.setMeta(Meta.builder().status(iqException.getHttpStatus().value()).errorCode(iqException.getErrorCode()).message(iqException.getMessage()).build());
        return ResponseEntity.status(iqException.getHttpStatus()).body(response);
    }
}
