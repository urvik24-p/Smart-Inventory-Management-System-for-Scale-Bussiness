package com.inventory.smartinventory.exception;

/**
 * Thrown when a requested resource (Product, SerialNumber, Transaction)
 * cannot be found in the database.
 *
 * Mapped to HTTP 404 Not Found by GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
