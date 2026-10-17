package com.inventory.smartinventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Maps to the stock_transaction_items table.
 *
 * Each row represents one product line within a stock transaction.
 * It records:
 *   - Which transaction this item belongs to
 *   - Which product was moved
 *   - Which specific serial number was moved
 *   - The quantity (always 1 per serial number, but stored for clarity)
 *
 * Relationship:
 *   StockTransaction (1) <-- (*) StockTransactionItem
 *   Product          (1) <-- (*) StockTransactionItem
 *   SerialNumber     (1) <-- (*) StockTransactionItem
 */
@Entity
@Table(name = "stock_transaction_items")
public class StockTransactionItem {

    // ============================================
    // PRIMARY KEY
    // ============================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    // ============================================
    // FOREIGN KEY - transaction_id
    // Back-reference to the parent transaction header
    // ============================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "items"})
    private StockTransaction transaction;

    // ============================================
    // FOREIGN KEY - product_id
    // The product being moved in this line item
    // ============================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;

    // ============================================
    // FOREIGN KEY - serial_id
    // The specific physical unit being moved
    // ============================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serial_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SerialNumber serialNumber;

    // ============================================
    // QUANTITY
    // Always 1 per serial number row, stored explicitly
    // ============================================

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    // ============================================
    // DEFAULT CONSTRUCTOR (required by JPA)
    // ============================================

    public StockTransactionItem() {
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public StockTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(StockTransaction transaction) {
        this.transaction = transaction;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SerialNumber getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(SerialNumber serialNumber) {
        this.serialNumber = serialNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
