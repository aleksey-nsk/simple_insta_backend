package com.example.simple_insta_backend.exception;

public class UserExistException extends RuntimeException {

    public UserExistException(String message) {
        super(message);
    }
}
