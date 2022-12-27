package com.example.simple_insta_backend.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<Object> handleUserExist(RuntimeException e) {
        String message = "User already exist: " + e.getMessage();
        log.error(message);
        Response response = new Response(message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PostNotFoundException.class, ImageNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(RuntimeException e) {
        String message = "Entity not found: " + e.getMessage();
        log.error(message);
        Response response = new Response(message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
