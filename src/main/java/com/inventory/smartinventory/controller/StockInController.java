package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.dto.StockInRequest;
import com.inventory.smartinventory.entity.StockTransaction;
import com.inventory.smartinventory.service.StockInService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Stock In operations.
 *
 * Base path: /api/stock-in
 *
 * Endpoint:
 *   POST /api/stock-in   ? receive new inventory, register serial numbers
 *
 * Request body example:
 *   {
 *     "productId": 3,
 *     "serialNumbers": ["SN-001", "SN-002", "SN-003"],
 *     "referenceNote": "PO-2024-001"
 *   }
 *
 * Response: the created StockTransaction record (HTTP 201)
 */
@RestController
@RequestMapping("/api/stock-in")
public class StockInController {

    private final StockInService stockInService;

    // Constructor injection
    public StockInController(StockInService stockInService) {
        this.stockInService = stockInService;
    }

    // ============================================
    // PROCESS STOCK IN
    // POST /api/stock-in
    // ============================================

    @PostMapping
    public ResponseEntity<StockTransaction> stockIn(
            @RequestBody StockInRequest request) {

        StockTransaction transaction = stockInService.processStockIn(request);

        return ResponseEntity.status(201).body(transaction);
    }
}
