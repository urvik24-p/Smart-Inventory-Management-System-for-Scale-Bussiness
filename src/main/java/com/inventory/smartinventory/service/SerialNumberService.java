package com.inventory.smartinventory.service;

import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.entity.SerialNumber;
import com.inventory.smartinventory.entity.SerialStatus;
import com.inventory.smartinventory.exception.DuplicateSerialNumberException;
import com.inventory.smartinventory.exception.InvalidOperationException;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.ProductRepository;
import com.inventory.smartinventory.repository.SerialNumberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic layer for serial number operations.
 *
 * Responsibilities:
 *   - Validate uniqueness before saving a new serial number
 *   - Resolve product by ID before linking a serial number to it
 *   - Provide queries by product and/or status
 *   - Allow status updates (e.g., mark as sold or damaged)
 */
@Service
public class SerialNumberService {

    private final SerialNumberRepository serialNumberRepository;
    private final ProductRepository productRepository;

    // ============================================
    // CONSTRUCTOR INJECTION
    // ============================================

    public SerialNumberService(SerialNumberRepository serialNumberRepository,
                               ProductRepository productRepository) {
        this.serialNumberRepository = serialNumberRepository;
        this.productRepository = productRepository;
    }

    // ============================================
    // GET ALL SERIAL NUMBERS
    // ============================================

    public List<SerialNumber> getAllSerialNumbers() {
        return serialNumberRepository.findAll();
    }

    // ============================================
    // GET SERIAL NUMBER BY ID
    // ============================================

    public Optional<SerialNumber> getSerialNumberById(int serialId) {
        return serialNumberRepository.findById(serialId);
    }

    // ============================================
    // GET SERIAL NUMBERS BY PRODUCT ID
    // ============================================

    public List<SerialNumber> getByProduct(int productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + productId));

        return serialNumberRepository.findByProduct(product);
    }

    // ============================================
    // GET SERIAL NUMBERS BY PRODUCT ID AND STATUS
    // ============================================

    public List<SerialNumber> getByProductAndStatus(int productId, SerialStatus status) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + productId));

        return serialNumberRepository.findByProductAndStatus(product, status);
    }

    // ============================================
    // ADD A SINGLE SERIAL NUMBER
    // ============================================

    public SerialNumber addSerialNumber(int productId, String serialNumberValue) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + productId));

        if (serialNumberRepository.existsBySerialNumber(serialNumberValue)) {
            throw new DuplicateSerialNumberException(serialNumberValue);
        }

        SerialNumber sn = new SerialNumber();
        sn.setProduct(product);
        sn.setSerialNumber(serialNumberValue);
        // status defaults to 'available' via the entity constructor

        return serialNumberRepository.save(sn);
    }

    // ============================================
    // UPDATE STATUS OF A SERIAL NUMBER
    // ============================================

    public SerialNumber updateStatus(int serialId, SerialStatus newStatus) {

        SerialNumber sn = serialNumberRepository.findById(serialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Serial number not found with ID: " + serialId));

        sn.setStatus(newStatus);

        return serialNumberRepository.save(sn);
    }

    // ============================================
    // DELETE A SERIAL NUMBER
    // Only permitted if the unit has never been sold
    // ============================================

    public void deleteSerialNumber(int serialId) {

        SerialNumber sn = serialNumberRepository.findById(serialId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Serial number not found with ID: " + serialId));

        if (sn.getStatus() == SerialStatus.sold) {
            throw new InvalidOperationException(
                    "Cannot delete a sold serial number: " + sn.getSerialNumber());
        }

        serialNumberRepository.deleteById(serialId);
    }
}
