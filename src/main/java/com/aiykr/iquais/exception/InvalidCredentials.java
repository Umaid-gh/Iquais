package com.aiykr.iquais.exception;

public class InvalidCredentials extends RuntimeException{
    private String message;
    public InvalidCredentials(String message){
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
