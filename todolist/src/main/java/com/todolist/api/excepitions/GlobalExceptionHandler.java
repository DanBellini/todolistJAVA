package com.todolist.api.excepitions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.todolist.api.excepitions.user_error.UserConflictException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({UserConflictException.class})
    public ResponseEntity<String> handlerUserConflict(UserConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}
