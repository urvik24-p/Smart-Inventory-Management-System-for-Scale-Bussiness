package com.inventory.smartinventory.service;

import com.inventory.smartinventory.dto.StockInRequest;
import com.inventory.smartinventory.entity.*;
import com.inventory.smartinventory.entity.StockTransaction.TransactionType;
import com.inventory.smartinventory.exception.DuplicateSerialNumberException;
import com.inventory.smartinventory.exception.InvalidOperationException;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.ProductRepository;
import com.inventory.smartinventory.repository.SerialNumberRepository;
import com.inventory.smartinventory.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

/**
 * Business logic for Stock In operations.
 *
 * A Stock In records incoming inventory.
 * For each serial number in the request:
 *   1. Validate the serial number does not already exist in the DB
 *   2. Create a new SerialNumber entity with status = available
 *   3. Create a StockTransactionItem linking transaction -> product -> serialNumber
 *
 * The entire operation is wrapped in a single DB transaction (@Transactional).
 * If any serial number is a duplicate the whole request is rolled back.
 */
@Service
public class StockInService {

    private final ProductRepository productRepository;
    private final SerialNumberRepository serialNumberRepository;
    private final StockTransactionRepository stockTransactionRepository;

    // Constructor injection
    public StockInService(ProductRepository productRepository,
                          SerialNumberRepository serialNumberRepository,
                          StockTransactionRepository stockTransactionRepository) {
        this.productRepository = productRepository;
        this.serialNumberRepository = serialNumberRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    // ============================================
    // PROCESS STOCK IN
    // ============================================

    @Transactional
    public StockTransaction processStockIn(StockInRequest request) {

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
        transaction.setTransactionType(TransactionType.IN);
        transaction.setReferenceNote(request.getReferenceNote());

        Set<String> requestSerials = new HashSet<>();
        // For each serial number: validate -> create SN -> create item
        for (String snValue : request.getSerialNumbers()) {
            String serialNumber = snValue.trim();
            if (!requestSerials.add(serialNumber)) {
                throw new InvalidOperationException("A serial number may appear only once per stock-in request: " + serialNumber);
            }

            if (serialNumberRepository.existsBySerialNumber(serialNumber)) {
                throw new DuplicateSerialNumberException(serialNumber);
            }

            SerialNumber sn = new SerialNumber();
            sn.setProduct(product);
            sn.setSerialNumber(serialNumber);
            SerialNumber savedSn = serialNumberRepository.save(sn);

            StockTransactionItem item = new StockTransactionItem();
            item.setTransaction(transaction);
            item.setProduct(product);
            item.setSerialNumber(savedSn);
            item.setQuantity(1);

            transaction.getItems().add(item);
        }

        return stockTransactionRepository.save(transaction);
    }
}
