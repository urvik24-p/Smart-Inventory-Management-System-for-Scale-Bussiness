package com.inventory.smartinventory.dto;

import com.inventory.smartinventory.entity.SerialStatus;
import jakarta.validation.constraints.NotNull;

public class SerialStatusRequest {
    @NotNull
    private SerialStatus status;
    public SerialStatus getStatus() { return status; }
    public void setStatus(SerialStatus status) { this.status = status; }
}
