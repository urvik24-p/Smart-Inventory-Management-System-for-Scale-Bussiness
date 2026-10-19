CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    brand VARCHAR(100),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE serial_numbers (
    serial_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    serial_number VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(12) NOT NULL DEFAULT 'available',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_serial_product FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE stock_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_type VARCHAR(10) NOT NULL,
    reference_note VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_transaction_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    product_id INT NOT NULL,
    serial_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_item_transaction FOREIGN KEY (transaction_id) REFERENCES stock_transactions(transaction_id),
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(product_id),
    CONSTRAINT fk_item_serial FOREIGN KEY (serial_id) REFERENCES serial_numbers(serial_id)
);

CREATE INDEX idx_serial_product_status ON serial_numbers(product_id, status);
CREATE INDEX idx_transaction_created_at ON stock_transactions(created_at);
CREATE INDEX idx_transaction_type_created ON stock_transactions(transaction_type, created_at);
CREATE INDEX idx_item_transaction ON stock_transaction_items(transaction_id);
