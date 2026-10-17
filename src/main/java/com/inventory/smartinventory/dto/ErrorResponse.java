package com.inventory.smartinventory.dto;

import java.time.LocalDateTime;

/**
 * Standard error response body returned by GlobalExceptionHandler.
 *
 * Example JSON:
 *   {
 *     "timestamp": "2024-05-10T14:30:00",
 *     "status": 404,
 *     "error": "Not Found",
 *     "message": "Product not found with ID: 99"
 *   }
 */
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(int status, String error, String message) {
        this.timestamp = LocalDateTime.now();
        this.status    = status;
        this.error     = error;
        this.message   = message;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus()              { return status; }
    public String getError()            { return error; }
    public String getMessage()          { return message; }
}
