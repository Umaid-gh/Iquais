package com.aiykr.iquais.exception;

import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = IquaisException.class)
    public ResponseEntity<Response<UserResponseDTO>> handleIquaisException(IquaisException iqException) {
        Response<UserResponseDTO> response = new Response<>();
        response.setMeta(Meta.builder().status(iqException.getHttpStatus().value()).errorCode(iqException.getErrorCode()).message(iqException.getMessage()).build());
        return ResponseEntity.status(iqException.getHttpStatus()).body(response);
    }
}
