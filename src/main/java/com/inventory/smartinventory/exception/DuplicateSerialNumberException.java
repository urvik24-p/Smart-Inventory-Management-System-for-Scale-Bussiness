package com.inventory.smartinventory.exception;

/**
 * Thrown when a Stock In request contains a serial number
 * that already exists in the database.
 *
 * Mapped to HTTP 409 Conflict by GlobalExceptionHandler.
 */
public class DuplicateSerialNumberException extends RuntimeException {

    public DuplicateSerialNumberException(String serialNumber) {
        super("Serial number already exists in inventory: " + serialNumber);
    }
}
