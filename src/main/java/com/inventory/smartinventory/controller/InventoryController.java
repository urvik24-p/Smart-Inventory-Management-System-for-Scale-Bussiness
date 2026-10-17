package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.dto.InventoryItem;
import com.inventory.smartinventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Current Inventory queries.
 *
 * Base path: /api/inventory
 *
 * Endpoints:
 *   GET /api/inventory               ? stock snapshot for all products
 *   GET /api/inventory/{productId}   ? stock snapshot for one product
 *
 * Response shape per product:
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
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // Constructor injection
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // ============================================
    // GET INVENTORY FOR ALL PRODUCTS
    // GET /api/inventory
    // ============================================

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllInventory() {

        List<InventoryItem> inventory = inventoryService.getAllInventory();

        return ResponseEntity.ok(inventory);
    }

    // ============================================
    // GET INVENTORY FOR ONE PRODUCT
    // GET /api/inventory/{productId}
    // ============================================

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItem> getInventoryByProduct(
            @PathVariable int productId) {

        InventoryItem item = inventoryService.getInventoryByProduct(productId);

        return ResponseEntity.ok(item);
    }
}
