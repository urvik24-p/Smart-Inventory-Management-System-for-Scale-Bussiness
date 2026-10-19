package com.inventory.smartinventory.dto;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO - body of a Stock In request.
 *
 * The client sends:
 *   {
 *     "productId": 3,
 *     "serialNumbers": ["SN-001", "SN-002"],
 *     "referenceNote": "PO-2024-001"   // optional
 *   }
 *
 * Rules enforced by StockInService:
 *   - productId must match an existing product
 *   - every serial number in the list must be globally unique (not yet in DB)
 *   - serialNumbers list must not be empty
 */
public class StockInRequest {

    // The product whose stock is being added
    @Positive
    private int productId;

    // List of new serial number strings to register
    @NotEmpty @Size(max = 500)
    private List<@NotBlank @Size(max = 100) String> serialNumbers;

    // Optional free-text: e.g. Purchase Order number, supplier name
    @Size(max = 255)
    private String referenceNote;

    // Default constructor required by Jackson
    public StockInRequest() {}

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public List<String> getSerialNumbers() { return serialNumbers; }
    public void setSerialNumbers(List<String> serialNumbers) { this.serialNumbers = serialNumbers; }

    public String getReferenceNote() { return referenceNote; }
    public void setReferenceNote(String referenceNote) { this.referenceNote = referenceNote; }
}
