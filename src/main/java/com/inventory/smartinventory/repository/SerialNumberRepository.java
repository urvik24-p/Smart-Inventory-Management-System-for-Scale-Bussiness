package com.inventory.smartinventory.repository;

import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.entity.SerialNumber;
import com.inventory.smartinventory.entity.SerialStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for serial_numbers table.
 *
 * Spring Data JPA generates all implementations automatically.
 * No SQL queries are required — method names follow JPA naming conventions.
 */
@Repository
public interface SerialNumberRepository extends JpaRepository<SerialNumber, Integer> {

    // ============================================
    // Find by serial number value (globally unique)
    // Used to look up a specific unit
    // ============================================
    Optional<SerialNumber> findBySerialNumber(String serialNumber);

    // ============================================
    // Check if a serial number value already exists
    // Used for uniqueness validation before saving
    // ============================================
    boolean existsBySerialNumber(String serialNumber);

    // ============================================
    // Find all serial numbers belonging to a product
    // ============================================
    List<SerialNumber> findByProduct(Product product);

    // ============================================
    // Find serial numbers belonging to a product filtered by status
    // Most important use: findByProductAndStatus(product, SerialStatus.available)
    // Required by Stock Out to get only sellable units
    // ============================================
    List<SerialNumber> findByProductAndStatus(Product product, SerialStatus status);
}
