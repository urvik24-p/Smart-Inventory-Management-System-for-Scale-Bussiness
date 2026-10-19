package com.inventory.smartinventory.repository;

import com.inventory.smartinventory.entity.StockTransaction;
import com.inventory.smartinventory.entity.StockTransaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for stock_transactions table.
 *
 * Spring Data JPA generates all implementations automatically.
 */
@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Integer> {

    @Override
    @EntityGraph(attributePaths = {"items", "items.product", "items.serialNumber"})
    List<StockTransaction> findAll();

    @Override
    @EntityGraph(attributePaths = {"items", "items.product", "items.serialNumber"})
    java.util.Optional<StockTransaction> findById(Integer transactionId);

    // ============================================
    // Find all transactions of a given type (IN or OUT)
    // Used by History Service to filter stock-in vs stock-out history
    // ============================================
    @EntityGraph(attributePaths = {"items", "items.product", "items.serialNumber"})
    List<StockTransaction> findByTransactionType(TransactionType transactionType);

    // ============================================
    // Find all transactions within a date range
    // Used by History Service for time-range filtering
    // ============================================
    @EntityGraph(attributePaths = {"items", "items.product", "items.serialNumber"})
    List<StockTransaction> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    // ============================================
    // Find by type AND within a date range
    // Combines both filters for targeted history queries
    // ============================================
    @EntityGraph(attributePaths = {"items", "items.product", "items.serialNumber"})
    List<StockTransaction> findByTransactionTypeAndCreatedAtBetween(
            TransactionType transactionType,
            LocalDateTime from,
            LocalDateTime to
    );
}
