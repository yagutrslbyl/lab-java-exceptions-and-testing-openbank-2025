package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> validationErrors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", validationErrors);

        return new org.springframework.http.ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingApiKeyException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleMissingApiKey(
            MissingApiKeyException exception
    ) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleMissingHeader(
            MissingRequestHeaderException exception
    ) {
        return buildErrorResponse("Missing required header: " + exception.getHeaderName(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleProductNotFound(
            ProductNotFoundException exception
    ) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPriceRangeException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleInvalidPriceRange(
            InvalidPriceRangeException exception
    ) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException exception
    ) {
        return buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    private org.springframework.http.ResponseEntity<Map<String, Object>> buildErrorResponse(
            String message,
            HttpStatus status
    ) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", message);

        return new org.springframework.http.ResponseEntity<>(response, status);
    }
}