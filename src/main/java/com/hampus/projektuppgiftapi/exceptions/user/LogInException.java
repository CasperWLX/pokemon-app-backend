package com.hampus.projektuppgiftapi.exceptions.user;

public class LogInException extends RuntimeException{
    public LogInException(String message) {
        super(message);
    }
}
