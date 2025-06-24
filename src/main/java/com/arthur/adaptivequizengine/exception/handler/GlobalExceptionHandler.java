package com.arthur.adaptivequizengine.exception.handler;

import com.arthur.adaptivequizengine.exception.dto.ApiErrorResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> userNotFoundException(UserNotFoundException exception) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String message) {
        ApiErrorResponse error = new ApiErrorResponse(
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }

}
