package com.aiykr.iquais.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class EmailSendingException extends IquaisException{
    public EmailSendingException(HttpStatus status, String code, String message) {
        super(status, code, message);
    }
}
