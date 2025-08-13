package com.todolist.api.excepitions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.todolist.api.excepitions.user_error.UserConflictException;
import com.todolist.api.excepitions.user_error.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
    
    // 1. Cria um map para armazenar os erros de validação.
        Map<String, String> errors = new HashMap<>();
    
    // 2. Itera sobre todos os erros contidos na exceção de validação.
    // A exceção 'MethodArgumentNotValidException' guarda uma lista de todos os erros de validação que ocorreram.
        exception.getBindingResult().getAllErrors().forEach(error -> {
    // 3. Obtém o nome do campo que falhou na validação.
    // O objeto 'error' é um 'FieldError', que contém informações sobre o campo.
            String fieldName = ((FieldError) error).getField();
        
    // 4. Obtém a mensagem de erro padrão que definimos no DTO (ex: '@Size(message = "...")').
            String errorMessage = error.getDefaultMessage();
        
    // 5. Adiciona o nome do campo e a mensagem de erro ao mapa.
            errors.put(fieldName, errorMessage);
        });
    
    // 6. Retorna o mapa de erros com o status HTTP 400 (Bad Request).
    // O status 400 indica que a requisição do cliente foi inválida devido aos dados incorretos.
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({UserConflictException.class})
    public ResponseEntity<String> handlerUserConflict(UserConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<String> handlerUserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

}
