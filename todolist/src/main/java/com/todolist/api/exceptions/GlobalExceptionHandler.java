package com.todolist.api.exceptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.todolist.api.enums.PriorityEnum;
import com.todolist.api.enums.StatusEnum;
import com.todolist.api.exceptions.task_error.TaskNotFoundException;
import com.todolist.api.exceptions.user_error.UserConflictException;
import com.todolist.api.exceptions.user_error.UserNotFoundException;

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
    
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormatException(InvalidFormatException ex) {
        String fieldName = null;
        if (ex.getPath() != null && !ex.getPath().isEmpty()) {
            fieldName = ex.getPath().get(0).getFieldName();
        }

        Map<String, Object> body = new HashMap<>();
        String message;

        if ("status".equals(fieldName)) {
            String validValues = Arrays.stream(StatusEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            message = String.format("The value '%s' is invalid for the 'status' field. The accepted values are: %s.",
                    ex.getValue(), validValues);
        } else if ("priority".equals(fieldName)) {
            String validValues = Arrays.stream(PriorityEnum.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            message = String.format("The value '%s' is invalid for the 'priority' field. The accepted values are: %s.",
                    ex.getValue(), validValues);
        } else {
            message = "Invalid field format: " + ex.getMessage();
        }

        body.put("error", "Invalid");
        body.put("message", message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler({UserConflictException.class})
    public ResponseEntity<String> handlerUserConflict(UserConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<String> handlerUserNotFound(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

        @ExceptionHandler({TaskNotFoundException.class})
    public ResponseEntity<String> handlerTaskNotFound(TaskNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }
}
