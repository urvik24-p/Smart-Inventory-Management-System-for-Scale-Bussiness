package com.inventory.smartinventory.controller;

import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.dto.ProductRequest;
import jakarta.validation.Valid;
import com.inventory.smartinventory.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // Constructor injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ============================================
    // GET ALL PRODUCTS
    // GET /api/products
    // ============================================
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        List<Product> products = productService.getAllProducts();

        return ResponseEntity.ok(products);
    }

    // ============================================
    // GET PRODUCT BY ID
    // GET /api/products/{id}
    // ============================================
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable int id) {

        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ============================================
    // ADD PRODUCT
    // POST /api/products
    // ============================================
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody ProductRequest product) {

        Product savedProduct = productService.addProduct(product.getProductName(), product.getCategory(), product.getBrand());

        return ResponseEntity.status(201).body(savedProduct);
    }

    // ============================================
    // UPDATE PRODUCT
    // PUT /api/products/{id}
    // ============================================
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductRequest product) {

        Product updatedProduct = productService.updateProduct(id, product.getProductName(), product.getCategory(), product.getBrand());

        return ResponseEntity.ok(updatedProduct);
    }

    // ============================================
    // DELETE PRODUCT
    // DELETE /api/products/{id}
    // ============================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable int id) {

        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
