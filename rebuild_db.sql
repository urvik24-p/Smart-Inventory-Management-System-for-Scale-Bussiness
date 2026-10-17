USE inventory;

-- Drop all tables (child tables first)
DROP TABLE IF EXISTS stock_transaction_items;
DROP TABLE IF EXISTS stock_transactions;
DROP TABLE IF EXISTS serial_numbers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS products;

-- 1. Products (matches Product.java)
CREATE TABLE products (
    product_id   INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category     VARCHAR(255),
    brand        VARCHAR(255),
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Serial numbers (matches SerialNumber.java)
CREATE TABLE serial_numbers (
    serial_id     INT AUTO_INCREMENT PRIMARY KEY,
    product_id    INT NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status        ENUM('available', 'sold', 'damaged') NOT NULL DEFAULT 'available',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- 3. Stock transactions (matches StockTransaction.java)
CREATE TABLE stock_transactions (
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    transaction_type ENUM('IN', 'OUT') NOT NULL,
    reference_note   VARCHAR(255),
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 4. Stock transaction items (matches StockTransactionItem.java)
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

-- 5. Users (matches AppUser.java)
CREATE TABLE users (
    user_id    INT AUTO_INCREMENT PRIMARY KEY,
    username   VARCHAR(50) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       ENUM('ADMIN', 'STAFF') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin (password = admin123)
INSERT INTO users (username, password, role) VALUES (
    'admin',
    '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36Kz2bKp4YwGj7gkGp3S2We',
    'ADMIN'
);

SELECT 'REBUILD COMPLETE' AS Status;
SHOW TABLES;
SELECT * FROM users;
