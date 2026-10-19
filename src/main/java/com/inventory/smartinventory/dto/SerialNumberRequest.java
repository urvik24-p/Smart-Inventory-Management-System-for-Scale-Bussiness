package com.inventory.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SerialNumberRequest {
    @NotBlank @Size(max = 100)
    private String serialNumber;
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
}
