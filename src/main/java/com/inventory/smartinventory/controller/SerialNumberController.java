package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.entity.SerialNumber;
import com.inventory.smartinventory.entity.SerialStatus;
import com.inventory.smartinventory.service.SerialNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for serial number operations.
 *
 * Base path: /api/serial-numbers
 *
 * Endpoints:
 *   GET    /api/serial-numbers                              ? all serial numbers
 *   GET    /api/serial-numbers/{id}                         ? one serial number by serialId
 *   GET    /api/serial-numbers/product/{productId}          ? all serials for a product
 *   GET    /api/serial-numbers/product/{productId}/status/{status} ? filtered by status
 *   POST   /api/serial-numbers/product/{productId}          ? add a serial number to a product
 *   PATCH  /api/serial-numbers/{id}/status                  ? update status of a serial number
 *   DELETE /api/serial-numbers/{id}                         ? delete a serial number
 */
@RestController
@RequestMapping("/api/serial-numbers")
public class SerialNumberController {

    private final SerialNumberService serialNumberService;

    // Constructor injection
    public SerialNumberController(SerialNumberService serialNumberService) {
        this.serialNumberService = serialNumberService;
    }

    // ============================================
    // GET ALL SERIAL NUMBERS
    // GET /api/serial-numbers
    // ============================================

    @GetMapping
    public ResponseEntity<List<SerialNumber>> getAllSerialNumbers() {

        List<SerialNumber> list = serialNumberService.getAllSerialNumbers();

        return ResponseEntity.ok(list);
    }

    // ============================================
    // GET SERIAL NUMBER BY ID
    // GET /api/serial-numbers/{id}
    // ============================================

    @GetMapping("/{id}")
    public ResponseEntity<SerialNumber> getSerialNumberById(
            @PathVariable int id) {

        return serialNumberService.getSerialNumberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================
    // GET ALL SERIALS FOR A PRODUCT
    // GET /api/serial-numbers/product/{productId}
    // ============================================

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<SerialNumber>> getByProduct(
            @PathVariable int productId) {

        List<SerialNumber> list = serialNumberService.getByProduct(productId);

        return ResponseEntity.ok(list);
    }

    // ============================================
    // GET SERIALS FOR A PRODUCT FILTERED BY STATUS
    // GET /api/serial-numbers/product/{productId}/status/{status}
    // Example: /product/3/status/available
    // ============================================

    @GetMapping("/product/{productId}/status/{status}")
    public ResponseEntity<List<SerialNumber>> getByProductAndStatus(
            @PathVariable int productId,
            @PathVariable SerialStatus status) {

        List<SerialNumber> list = serialNumberService.getByProductAndStatus(productId, status);

        return ResponseEntity.ok(list);
    }

    // ============================================
    // ADD SERIAL NUMBER TO A PRODUCT
    // POST /api/serial-numbers/product/{productId}
    // Body: { "serialNumber": "SN-ABC-001" }
    // ============================================

    @PostMapping("/product/{productId}")
    public ResponseEntity<SerialNumber> addSerialNumber(
            @PathVariable int productId,
            @RequestBody Map<String, String> body) {

        String serialNumberValue = body.get("serialNumber");

        SerialNumber saved = serialNumberService.addSerialNumber(productId, serialNumberValue);

        return ResponseEntity.status(201).body(saved);
    }

    // ============================================
    // UPDATE STATUS OF A SERIAL NUMBER
    // PATCH /api/serial-numbers/{id}/status
    // Body: { "status": "damaged" }
    // ============================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<SerialNumber> updateStatus(
            @PathVariable int id,
            @RequestBody Map<String, String> body) {

        SerialStatus newStatus = SerialStatus.valueOf(body.get("status"));

        SerialNumber updated = serialNumberService.updateStatus(id, newStatus);

        return ResponseEntity.ok(updated);
    }

    // ============================================
    // DELETE A SERIAL NUMBER
    // DELETE /api/serial-numbers/{id}
    // Rejected if the unit has status = sold
    // ============================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSerialNumber(
            @PathVariable int id) {

        serialNumberService.deleteSerialNumber(id);

        return ResponseEntity.noContent().build();
    }
}
