package com.aiykr.iquais.exception;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Custom exception class for representing application-specific
 * exceptions with HTTP status, message, and error code.
 */
@ApiModel(description = "Custom Exception for Iquais Application")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IquaisException extends Exception{
    /**
     * The HTTP status associated with the exception.
     */
    private HttpStatus httpStatus;
    /**
     * The error message providing more details about the exception.
     */
    private String message;
    /**
     * The error code representing the specific exception scenario.
     */
    private String errorCode;
}
