package com.inventory.smartinventory.service;

import com.inventory.smartinventory.dto.InventoryItem;
import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.entity.SerialNumber;
import com.inventory.smartinventory.entity.SerialStatus;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.ProductRepository;
import com.inventory.smartinventory.repository.SerialNumberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Read-only service that computes the current stock snapshot.
 *
 * Derives live stock counts from the serial_numbers table.
 * No extra DB columns needed.
 */
@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final SerialNumberRepository serialNumberRepository;

    public InventoryService(ProductRepository productRepository,
                            SerialNumberRepository serialNumberRepository) {
        this.productRepository = productRepository;
        this.serialNumberRepository = serialNumberRepository;
    }

    // Get inventory for all products
    public List<InventoryItem> getAllInventory() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::buildInventoryItem)
                .collect(Collectors.toList());
    }

    // Get inventory for one product
    public InventoryItem getInventoryByProduct(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + productId));
        return buildInventoryItem(product);
    }

    // Count each status group for a product
    private InventoryItem buildInventoryItem(Product product) {
        List<SerialNumber> allUnits = serialNumberRepository.findByProduct(product);

        int available = (int) allUnits.stream()
                .filter(sn -> sn.getStatus() == SerialStatus.available).count();
        int sold = (int) allUnits.stream()
                .filter(sn -> sn.getStatus() == SerialStatus.sold).count();
        int damaged = (int) allUnits.stream()
                .filter(sn -> sn.getStatus() == SerialStatus.damaged).count();

        return new InventoryItem(
                product.getProductId(),
                product.getProductName(),
                product.getBrand(),
                product.getCategory(),
                available, sold, damaged
        );
    }
}
