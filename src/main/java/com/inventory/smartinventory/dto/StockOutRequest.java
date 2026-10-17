package com.inventory.smartinventory.dto;

import java.util.List;

/**
 * DTO - body of a Stock Out request.
 *
 * The client sends:
 *   {
 *     "productId": 3,
 *     "serialNumbers": ["SN-001", "SN-002"],
 *     "referenceNote": "Customer: John Doe"   // optional
 *   }
 *
 * Rules enforced by StockOutService:
 *   - productId must match an existing product
 *   - every serial number must exist in the DB
 *   - every serial number must currently have status = available
 *   - serialNumbers list must not be empty
 */
public class StockOutRequest {

    // The product whose stock is being dispatched
    private int productId;

    // Serial numbers to mark as sold
    private List<String> serialNumbers;

    // Optional free-text: customer name, invoice number, etc.
    private String referenceNote;

    // Default constructor required by Jackson
    public StockOutRequest() {}

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public List<String> getSerialNumbers() { return serialNumbers; }
    public void setSerialNumbers(List<String> serialNumbers) { this.serialNumbers = serialNumbers; }

    public String getReferenceNote() { return referenceNote; }
    public void setReferenceNote(String referenceNote) { this.referenceNote = referenceNote; }
}
