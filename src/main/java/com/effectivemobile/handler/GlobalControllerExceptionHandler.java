package com.effectivemobile.handler;

import com.effectivemobile.dto.response.ErrorResponse;
import com.effectivemobile.dto.response.ErrorResponseWithParam;
import com.effectivemobile.exception.ObjectAlreadyExistsException;
import com.effectivemobile.exception.RefreshTokenException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseWithParam> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> params = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            params.put(fieldName, errorMessage);
        });
        String error = "Not valid fields";
        log.error(error);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseWithParam.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .error(error)
                        .params(params).build());
    }

    @ExceptionHandler(ObjectAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleObjectAlreadyExistsException(ObjectAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleRefreshTokenException(RefreshTokenException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .error(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.builder()
                        .status(HttpStatus.FORBIDDEN)
                        .error(ex.getMessage())
                        .build());
    }
}