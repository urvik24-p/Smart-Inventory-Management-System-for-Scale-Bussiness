package com.inventory.smartinventory.service;

import com.inventory.smartinventory.entity.StockTransaction;
import com.inventory.smartinventory.entity.StockTransaction.TransactionType;
import com.inventory.smartinventory.exception.InvalidOperationException;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Read-only service for querying stock transaction history.
 */
@Service
public class StockHistoryService {

    private final StockTransactionRepository stockTransactionRepository;

    public StockHistoryService(StockTransactionRepository stockTransactionRepository) {
        this.stockTransactionRepository = stockTransactionRepository;
    }

    // Get all transactions
    public List<StockTransaction> getAllTransactions() {
        return stockTransactionRepository.findAll();
    }

    // Get transaction by ID (with items)
    public Optional<StockTransaction> getTransactionById(int transactionId) {
        return stockTransactionRepository.findById(transactionId);
    }

    // Get by type: IN or OUT
    public List<StockTransaction> getByType(String type) {
        TransactionType transactionType = parseType(type);
        return stockTransactionRepository.findByTransactionType(transactionType);
    }

    // Get by date range (inclusive)
    public List<StockTransaction> getByDateRange(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end   = to.atTime(23, 59, 59);
        return stockTransactionRepository.findByCreatedAtBetween(start, end);
    }

    // Get by type + date range
    public List<StockTransaction> getByTypeAndDateRange(String type, LocalDate from, LocalDate to) {
        TransactionType transactionType = parseType(type);
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end   = to.atTime(23, 59, 59);
        return stockTransactionRepository.findByTransactionTypeAndCreatedAtBetween(
                transactionType, start, end);
    }

    // Parse "IN"/"OUT" string to enum safely
    private TransactionType parseType(String type) {
        try {
            return TransactionType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidOperationException(
                    "Invalid transaction type: '" + type + "'. Use IN or OUT.");
        }
    }
}
