package com.ecommerece.store.exception;

import com.ecommerece.store.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e) {
        String message = "You don't have permissions to perform this action";

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body( new ApiResponse(message, null));
    }
}
