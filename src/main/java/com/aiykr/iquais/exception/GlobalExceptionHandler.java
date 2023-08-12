package com.aiykr.iquais.exception;

import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<Response> handleStatusException(HttpServletRequest request, ResponseStatusException responseException) {
        Response response = new Response();
        response.setMeta(Meta.builder().status(responseException.getStatus().value()).message(responseException.getReason()).build());
        return ResponseEntity.status(responseException.getStatus()).body(response);
    }
}
