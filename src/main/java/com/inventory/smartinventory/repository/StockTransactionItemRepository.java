package com.inventory.smartinventory.repository;

import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.entity.SerialNumber;
import com.inventory.smartinventory.entity.StockTransaction;
import com.inventory.smartinventory.entity.StockTransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for stock_transaction_items table.
 *
 * Spring Data JPA generates all implementations automatically.
 */
@Repository
public interface StockTransactionItemRepository extends JpaRepository<StockTransactionItem, Integer> {

    // ============================================
    // Find all items belonging to one transaction
    // Used when loading full transaction details
    // ============================================
    List<StockTransactionItem> findByTransaction(StockTransaction transaction);

    // ============================================
    // Find all line items for a specific product
    // Useful for per-product movement history
    // ============================================
    List<StockTransactionItem> findByProduct(Product product);

    // ============================================
    // Find the item record for a specific serial number
    // Used to trace which transaction moved a given unit
    // ============================================
    Optional<StockTransactionItem> findBySerialNumber(SerialNumber serialNumber);
}
