package com.aiykr.iquais.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IquaisException extends Exception{

    private HttpStatus httpStatus;
    private String message;
    private String errorCode;
}
