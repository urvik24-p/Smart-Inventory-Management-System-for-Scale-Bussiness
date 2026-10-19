package com.inventory.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductRequest {
    @NotBlank @Size(max = 255)
    private String productName;
    @Size(max = 100)
    private String category;
    @Size(max = 100)
    private String brand;
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}
