package com.inventory.smartinventory.dto;

/**
 * DTO - represents the current stock snapshot for one product.
 *
 * Returned by InventoryController so the frontend gets
 * all product info and stock counts in one response object.
 *
 * Example JSON:
 *   {
 *     "productId": 3,
 *     "productName": "Laptop X1",
 *     "brand": "Lenovo",
 *     "category": "Electronics",
 *     "availableCount": 12,
 *     "soldCount": 5,
 *     "damagedCount": 1,
 *     "totalCount": 18
 *   }
 */
public class InventoryItem {

    private int productId;
    private String productName;
    private String brand;
    private String category;

    // Stock counts by status
    private int availableCount;
    private int soldCount;
    private int damagedCount;
    private int totalCount;

    // Default constructor required by Jackson
    public InventoryItem() {}

    // Convenience constructor
    public InventoryItem(int productId, String productName, String brand, String category,
                         int availableCount, int soldCount, int damagedCount) {
        this.productId      = productId;
        this.productName    = productName;
        this.brand          = brand;
        this.category       = category;
        this.availableCount = availableCount;
        this.soldCount      = soldCount;
        this.damagedCount   = damagedCount;
        this.totalCount     = availableCount + soldCount + damagedCount;
    }

    // ============================================
    // GETTERS AND SETTERS
    // ============================================

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getAvailableCount() { return availableCount; }
    public void setAvailableCount(int availableCount) { this.availableCount = availableCount; }

    public int getSoldCount() { return soldCount; }
    public void setSoldCount(int soldCount) { this.soldCount = soldCount; }

    public int getDamagedCount() { return damagedCount; }
    public void setDamagedCount(int damagedCount) { this.damagedCount = damagedCount; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
}
