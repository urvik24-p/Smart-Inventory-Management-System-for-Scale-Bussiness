-- ============================================
-- SmartInventory — Complete Database Setup
-- Run this ONCE to create everything from scratch
-- ============================================

CREATE DATABASE IF NOT EXISTS inventory;
USE inventory;

-- Drop tables in correct order (child tables first)
DROP TABLE IF EXISTS stock_transaction_items;
DROP TABLE IF EXISTS stock_transactions;
DROP TABLE IF EXISTS serial_numbers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS products;

-- 1. Products table
CREATE TABLE products (
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category     VARCHAR(255),
    brand        VARCHAR(255),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Serial numbers (each physical unit of a product)
CREATE TABLE serial_numbers (
    serial_id     INT AUTO_INCREMENT PRIMARY KEY,
    product_id    INT NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status        ENUM('available', 'sold', 'damaged') NOT NULL DEFAULT 'available',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- 3. Stock transactions (header record for each stock movement)
CREATE TABLE stock_transactions (
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    transaction_type ENUM('IN', 'OUT') NOT NULL,
    reference_note   VARCHAR(255),
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 4. Stock transaction items (detail: which serial was moved)
CREATE TABLE stock_transaction_items (
    item_id        INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    product_id     INT NOT NULL,
    serial_id      INT NOT NULL,
    quantity       INT NOT NULL DEFAULT 1,
    FOREIGN KEY (transaction_id) REFERENCES stock_transactions(transaction_id),
    FOREIGN KEY (product_id)     REFERENCES products(product_id),
    FOREIGN KEY (serial_id)      REFERENCES serial_numbers(serial_id)
);

-- 5. Users table (login credentials)
CREATE TABLE users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('ADMIN', 'STAFF') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin user (password = admin123 as BCrypt hash)
INSERT INTO users (username, password, role) VALUES (
    'admin',
    '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36Kz2bKp4YwGj7gkGp3S2We',
    'ADMIN'
);

-- Verify setup
SELECT 'DATABASE SETUP COMPLETE' AS Status;
SHOW TABLES;
SELECT user_id, username, role FROM users;
