package com.inventory.smartinventory.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Maps to the stock_transactions table.
 *
 * A StockTransaction is the header record for one stock movement event.
 * It records WHO performed the action, WHEN, and WHAT TYPE (IN or OUT).
 *
 * Each transaction contains one or more StockTransactionItems,
 * each of which records one product + the serial numbers moved.
 *
 * Relationship:
 *   StockTransaction (1) <-- (*) StockTransactionItem
 */
@Entity
@Table(name = "stock_transactions")
public class StockTransaction {

    // ============================================
    // PRIMARY KEY
    // ============================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    // ============================================
    // TRANSACTION TYPE
    // Stored as string: "IN" or "OUT"
    // ============================================

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 10)
    private TransactionType transactionType;

    // ============================================
    // REFERENCE / NOTES
    // Optional free-text: PO number, customer name, etc.
    // ============================================

    @Column(name = "reference_note", length = 255)
    private String referenceNote;

    // ============================================
    // TIMESTAMP
    // ============================================

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ============================================
    // ITEMS BELONGING TO THIS TRANSACTION
    // CascadeType.ALL ? saving the transaction saves its items too
    // orphanRemoval   ? removing an item from the list deletes it from DB
    // ============================================

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockTransactionItem> items = new ArrayList<>();

    // ============================================
    // DEFAULT CONSTRUCTOR (required by JPA)
    // ============================================

    public StockTransaction() {
        this.createdAt = LocalDateTime.now();
    }

    // ============================================
    // INNER ENUM - transaction type
    // Defined here so it lives alongside the entity
    // ============================================

    public enum TransactionType {
        IN, OUT
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getReferenceNote() {
        return referenceNote;
    }

    public void setReferenceNote(String referenceNote) {
        this.referenceNote = referenceNote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<StockTransactionItem> getItems() {
        return items;
    }

    public void setItems(List<StockTransactionItem> items) {
        this.items = items;
    }
}
