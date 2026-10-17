package com.inventory.smartinventory.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Maps to the serial_numbers table.
 *
 * Each row represents one physical unit of a product.
 * A serial number is globally unique across all products.
 *
 * Relationship:
 *   Product (1) ←—— (*) SerialNumber
 */
@Entity
@Table(name = "serial_numbers")
public class SerialNumber {

    // ============================================
    // PRIMARY KEY
    // ============================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serial_id")
    private int serialId;

    // ============================================
    // FOREIGN KEY — product_id
    // Many serial numbers belong to one product
    // ============================================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;

    // ============================================
    // SERIAL NUMBER VALUE
    // Globally unique across all products
    // ============================================

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    // ============================================
    // STATUS
    // Stored as a string matching MySQL ENUM column
    // ============================================

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SerialStatus status;

    // ============================================
    // TIMESTAMP
    // ============================================

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ============================================
    // DEFAULT CONSTRUCTOR (required by JPA)
    // Sets default status = available
    // Sets createdAt = now
    // ============================================

    public SerialNumber() {
        this.status = SerialStatus.available;
        this.createdAt = LocalDateTime.now();
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public int getSerialId() {
        return serialId;
    }

    public void setSerialId(int serialId) {
        this.serialId = serialId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public SerialStatus getStatus() {
        return status;
    }

    public void setStatus(SerialStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
