package com.aiykr.iquais.exception;

import com.aiykr.iquais.dto.response.Meta;
import com.aiykr.iquais.dto.response.Response;
import com.aiykr.iquais.dto.response.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling custom IquaisException.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles IquaisException and returns a customized response.
     *
     * @param iqException The IquaisException to be handled.
     * @return A ResponseEntity containing a response with error details.
     */
    @ExceptionHandler(value = IquaisException.class)
    public ResponseEntity<Response<UserResponseDTO>> handleIquaisException(IquaisException iqException) {
        Response<UserResponseDTO> response = new Response<>();
        response.setMeta(Meta.builder().statusCode(iqException.getHttpStatus().value()).errorCode(iqException.getErrorCode()).message(iqException.getMessage()).build());
        return ResponseEntity.status(iqException.getHttpStatus()).body(response);
    }

    @ExceptionHandler(value = EmailSendingException.class)
    public ResponseEntity<Response<UserResponseDTO>> handleMsgException(EmailSendingException iqException) {
        Response<UserResponseDTO> response = new Response<>();
        response.setMeta(Meta.builder().statusCode(iqException.getHttpStatus().value()).errorCode(iqException.getErrorCode()).message(iqException.getMessage()).build());
        return ResponseEntity.status(iqException.getHttpStatus()).body(response);
    }
}
