package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.entity.StockTransaction;
import com.inventory.smartinventory.service.StockHistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for Stock History queries.
 *
 * Base path: /api/history
 *
 * Endpoints:
 *   GET /api/history                            ? all transactions
 *   GET /api/history/{id}                       ? one transaction with its items
 *   GET /api/history?type=IN                    ? filter by type (IN or OUT)
 *   GET /api/history?from=2024-01-01&to=2024-12-31          ? filter by date range
 *   GET /api/history?type=OUT&from=2024-01-01&to=2024-12-31 ? both filters combined
 *
 * All date parameters use ISO format: yyyy-MM-dd
 */
@RestController
@RequestMapping("/api/history")
public class StockHistoryController {

    private final StockHistoryService stockHistoryService;

    // Constructor injection
    public StockHistoryController(StockHistoryService stockHistoryService) {
        this.stockHistoryService = stockHistoryService;
    }

    // ============================================
    // GET ALL / FILTERED TRANSACTIONS
    // GET /api/history
    // Optional query params: type, from, to
    // ============================================

    @GetMapping
    public ResponseEntity<List<StockTransaction>> getHistory(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<StockTransaction> result;

        // Both type and date range provided
        if (type != null && from != null && to != null) {
            result = stockHistoryService.getByTypeAndDateRange(type, from, to);

        // Only type provided
        } else if (type != null) {
            result = stockHistoryService.getByType(type);

        // Only date range provided
        } else if (from != null && to != null) {
            result = stockHistoryService.getByDateRange(from, to);

        // No filter - return everything
        } else {
            result = stockHistoryService.getAllTransactions();
        }

        return ResponseEntity.ok(result);
    }

    // ============================================
    // GET SINGLE TRANSACTION BY ID
    // GET /api/history/{id}
    // Returns the transaction header + all its line items
    // ============================================

    @GetMapping("/{id}")
    public ResponseEntity<StockTransaction> getTransactionById(
            @PathVariable int id) {

        return stockHistoryService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
