package com.inventory.smartinventory.exception;

/**
 * Thrown when a business rule is violated, such as:
 *   - Trying to sell a serial number that is not available
 *   - Trying to delete a sold serial number
 *   - Providing an empty serial number list
 *   - Serial number does not belong to the given product
 *
 * Mapped to HTTP 400 Bad Request by GlobalExceptionHandler.
 */
public class InvalidOperationException extends RuntimeException {

    public InvalidOperationException(String message) {
        super(message);
    }
}
