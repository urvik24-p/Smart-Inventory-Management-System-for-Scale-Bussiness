package com.inventory.smartinventory.service;

import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.ProductRepository;
import com.inventory.smartinventory.repository.SerialNumberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SerialNumberRepository serialNumberRepository;

    // Constructor injection
    public ProductService(ProductRepository productRepository, SerialNumberRepository serialNumberRepository) {
        this.productRepository = productRepository;
        this.serialNumberRepository = serialNumberRepository;
    }

    // Get all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get one product by ID
    public Optional<Product> getProductById(int productId) {
        return productRepository.findById(productId);
    }

    // Add a new product
    public Product addProduct(String productName, String category, String brand) {
        Product product = new Product();
        product.setProductName(productName.trim());
        product.setCategory(normalize(category));
        product.setBrand(normalize(brand));
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(int productId, String productName, String category, String brand) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + productId));

        existingProduct.setProductName(productName.trim());
        existingProduct.setCategory(normalize(category));
        existingProduct.setBrand(normalize(brand));

        return productRepository.save(existingProduct);
    }

    // Delete a product
    public void deleteProduct(int productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
        if (serialNumberRepository.countByProduct(product) > 0) {
            throw new com.inventory.smartinventory.exception.InvalidOperationException(
                    "Products with serial-number history cannot be deleted. Archive them instead.");
        }
        productRepository.delete(product);
    }

    private String normalize(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
