package com.harshit.project.taskTrackingApp.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

// HANDLES AUTHENTICATION FAILURES
 @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredential(RuntimeException e){
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        if(e.getMessage().contains("not found")){
            httpStatus = HttpStatus.NOT_FOUND;

        } else if (e.getMessage().contains("invalid")) {
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(Map.of("error", e.getMessage()), httpStatus);
    }

    // Handles JWT validation errors
    @ExceptionHandler(io.jsonwebtoken.JwtException.class)
    public ResponseEntity<Map<String, String>> handleJwtException(io.jsonwebtoken.JwtException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    // Handles user not found or other runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    // Fallback for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOtherExceptions(Exception ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
