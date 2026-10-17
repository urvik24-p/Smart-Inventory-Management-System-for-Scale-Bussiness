package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.dto.StockOutRequest;
import com.inventory.smartinventory.entity.StockTransaction;
import com.inventory.smartinventory.service.StockOutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Stock Out operations.
 *
 * Base path: /api/stock-out
 *
 * Endpoint:
 *   POST /api/stock-out   ? dispatch inventory, mark serial numbers as sold
 *
 * Request body example:
 *   {
 *     "productId": 3,
 *     "serialNumbers": ["SN-001", "SN-002"],
 *     "referenceNote": "Customer: John Doe"
 *   }
 *
 * Response: the created StockTransaction record (HTTP 201)
 *
 * Fails with 500 if:
 *   - Product does not exist
 *   - Any serial number does not exist
 *   - Any serial number does not belong to the given product
 *   - Any serial number is not currently available (already sold / damaged)
 */
@RestController
@RequestMapping("/api/stock-out")
public class StockOutController {

    private final StockOutService stockOutService;

    // Constructor injection
    public StockOutController(StockOutService stockOutService) {
        this.stockOutService = stockOutService;
    }

    // ============================================
    // PROCESS STOCK OUT
    // POST /api/stock-out
    // ============================================

    @PostMapping
    public ResponseEntity<StockTransaction> stockOut(
            @RequestBody StockOutRequest request) {

        StockTransaction transaction = stockOutService.processStockOut(request);

        return ResponseEntity.status(201).body(transaction);
    }
}
