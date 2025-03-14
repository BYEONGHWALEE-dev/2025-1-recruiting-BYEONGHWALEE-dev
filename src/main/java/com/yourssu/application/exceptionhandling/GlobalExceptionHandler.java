package com.yourssu.application.exceptionhandling;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
