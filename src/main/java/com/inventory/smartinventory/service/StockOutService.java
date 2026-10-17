package com.inventory.smartinventory.service;

import com.inventory.smartinventory.dto.StockOutRequest;
import com.inventory.smartinventory.entity.*;
import com.inventory.smartinventory.entity.StockTransaction.TransactionType;
import com.inventory.smartinventory.exception.InvalidOperationException;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.ProductRepository;
import com.inventory.smartinventory.repository.SerialNumberRepository;
import com.inventory.smartinventory.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for Stock Out operations.
 *
 * A Stock Out records outgoing inventory (a sale or dispatch).
 * For each serial number in the request:
 *   1. Validate the serial number exists in the DB
 *   2. Validate the serial number belongs to the given product
 *   3. Validate the serial number currently has status = available
 *   4. Mark the serial number status as sold
 *   5. Create a StockTransactionItem
 *
 * The entire operation is wrapped in a single DB transaction (@Transactional).
 */
@Service
public class StockOutService {

    private final ProductRepository productRepository;
    private final SerialNumberRepository serialNumberRepository;
    private final StockTransactionRepository stockTransactionRepository;

    // Constructor injection
    public StockOutService(ProductRepository productRepository,
                           SerialNumberRepository serialNumberRepository,
                           StockTransactionRepository stockTransactionRepository) {
        this.productRepository = productRepository;
        this.serialNumberRepository = serialNumberRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    // ============================================
    // PROCESS STOCK OUT
    // ============================================

    @Transactional
    public StockTransaction processStockOut(StockOutRequest request) {

        // Validate non-empty list
        if (request.getSerialNumbers() == null || request.getSerialNumbers().isEmpty()) {
            throw new InvalidOperationException("Serial numbers list must not be empty.");
        }

        // Resolve product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + request.getProductId()));

        // Build the transaction header
        StockTransaction transaction = new StockTransaction();
        transaction.setTransactionType(TransactionType.OUT);
        transaction.setReferenceNote(request.getReferenceNote());

        // For each serial number: validate -> mark sold -> build item
        for (String snValue : request.getSerialNumbers()) {

            // Must exist in the DB
            SerialNumber sn = serialNumberRepository.findBySerialNumber(snValue)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Serial number not found: " + snValue));

            // Must belong to the specified product
            if (sn.getProduct().getProductId() != product.getProductId()) {
                throw new InvalidOperationException(
                        "Serial number '" + snValue + "' does not belong to product ID: "
                                + request.getProductId());
            }

            // Must be available (not already sold or damaged)
            if (sn.getStatus() != SerialStatus.available) {
                throw new InvalidOperationException(
                        "Serial number '" + snValue + "' is not available. Current status: "
                                + sn.getStatus());
            }

            // Mark as sold
            sn.setStatus(SerialStatus.sold);
            serialNumberRepository.save(sn);

            // Build transaction item
            StockTransactionItem item = new StockTransactionItem();
            item.setTransaction(transaction);
            item.setProduct(product);
            item.setSerialNumber(sn);
            item.setQuantity(1);

            transaction.getItems().add(item);
        }

        return stockTransactionRepository.save(transaction);
    }
}
