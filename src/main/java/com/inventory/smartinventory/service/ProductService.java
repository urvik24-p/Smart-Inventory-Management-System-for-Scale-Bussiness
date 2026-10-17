package com.inventory.smartinventory.service;

import com.inventory.smartinventory.entity.Product;
import com.inventory.smartinventory.exception.ResourceNotFoundException;
import com.inventory.smartinventory.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    // Constructor injection
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Update an existing product
    public Product updateProduct(int productId, Product updatedProduct) {

        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + productId));

        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setBrand(updatedProduct.getBrand());

        return productRepository.save(existingProduct);
    }

    // Delete a product
    public void deleteProduct(int productId) {

        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with ID: " + productId);
        }

        productRepository.deleteById(productId);
    }
}
