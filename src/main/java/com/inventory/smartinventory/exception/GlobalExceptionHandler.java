package com.inventory.smartinventory.exception;

import com.inventory.smartinventory.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralised exception handler for all REST controllers.
 *
 * Intercepts custom exceptions and converts them into consistent
 * JSON error responses with appropriate HTTP status codes.
 *
 * Mapping:
 *   ResourceNotFoundException       -> 404 Not Found
 *   DuplicateSerialNumberException  -> 409 Conflict
 *   InvalidOperationException       -> 400 Bad Request
 *   Exception (catch-all)           -> 500 Internal Server Error
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ============================================
    // 404 NOT FOUND
    // Product, SerialNumber, or Transaction not in DB
    // ============================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    // ============================================
    // 409 CONFLICT
    // Duplicate serial number on Stock In
    // ============================================

    @ExceptionHandler(DuplicateSerialNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateSerialNumberException ex) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // ============================================
    // 400 BAD REQUEST
    // Business rule violation (wrong status, empty list, wrong product)
    // ============================================

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOperation(InvalidOperationException ex) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ============================================
    // 500 INTERNAL SERVER ERROR
    // Catch-all for any unexpected exception
    // ============================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {

        ErrorResponse body = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
