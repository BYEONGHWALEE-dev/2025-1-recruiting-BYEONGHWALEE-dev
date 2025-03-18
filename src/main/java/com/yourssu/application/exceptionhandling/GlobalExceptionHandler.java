package com.yourssu.application.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserErrorResponse> handleUserNotFoundException(
            UserNotFoundException error, HttpServletRequest request) {

        // requestURL 가져오기
        String requestURL = request.getRequestURL().toString();

        UserErrorResponse errorResponse = new UserErrorResponse(
                System.currentTimeMillis(),HttpStatus.NOT_FOUND.value(), error.getMessage(), requestURL);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ConstraintViolationException ex
    , HttpServletRequest request) {

        String errorMessage = ex.getConstraintViolations().iterator().next().getMessage();
        String requestURL = request.getRequestURL().toString();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", errorMessage);
        response.put("requestURL", requestURL);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserErrorResponse> handleALLException(
            Exception error, HttpServletRequest request) {

        // requestURL 가져오기
        String requestURL = request.getRequestURL().toString();

        UserErrorResponse errorResponse = new UserErrorResponse(
                System.currentTimeMillis(),HttpStatus.BAD_REQUEST.value(), error.getMessage(), requestURL);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
