-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS stores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    store_name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    store_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (store_id) REFERENCES stores(id)
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- FLAW: duplicate CREATE TABLE for products (will silently succeed due to IF NOT EXISTS)
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL
);

-- Create customers table (FLAW: password stored as plain VARCHAR)
CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    password VARCHAR(255) NOT NULL,  -- FLAW: plain text password
    address VARCHAR(500),
    loyalty_points VARCHAR(50),      -- FLAW: points stored as VARCHAR
    active BOOLEAN DEFAULT TRUE
);

-- Create reviews table (FLAW: rating as VARCHAR instead of INT)
CREATE TABLE IF NOT EXISTS reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(100) NOT NULL,
    product_id BIGINT NOT NULL,
    rating VARCHAR(5) NOT NULL,      -- FLAW: VARCHAR for rating, no CHECK constraint
    comment TEXT,
    created_at TIMESTAMP,            -- FLAW: nullable timestamp
    status VARCHAR(20)               -- FLAW: no CHECK constraint on status values
);

-- Create coupons table (FLAW: applicable_categories as comma-separated string)
CREATE TABLE IF NOT EXISTS coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,       -- FLAW: no UNIQUE constraint
    type VARCHAR(50),
    discount_value DOUBLE,
    minimum_order_amount DOUBLE,
    expiry_date DATE,
    usage_limit INT DEFAULT 0,
    used_count INT DEFAULT 0,
    active BOOLEAN DEFAULT TRUE,
    applicable_categories VARCHAR(500),
    stackable BOOLEAN DEFAULT FALSE
);

-- Create discounts table (FLAW: both percentage and amount columns — ambiguous)
CREATE TABLE IF NOT EXISTS discounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    discount_code VARCHAR(50),
    discount_name VARCHAR(255),
    discount_percentage DOUBLE,
    discount_amount DOUBLE,
    start_date DATE,
    end_date DATE,
    target_category VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE
);

-- Insert Singapore stores
INSERT INTO stores (store_name, location) VALUES
('Ceylon Tea House Orchard', 'Orchard Road, Singapore'),
('Ceylon Tea House Marina', 'Marina Bay, Singapore'),
('Ceylon Tea House Sentosa', 'Sentosa Island, Singapore');

-- Insert tea products
INSERT INTO products (product_code, name, category, price) VALUES
('TEA-001', 'Ceylon Black Tea', 'Black Tea', 12.99),
('TEA-002', 'Ceylon Green Tea', 'Green Tea', 14.99),
('TEA-003', 'Ceylon Earl Grey', 'Flavored Tea', 16.99),
('TEA-004', 'Ceylon Jasmine Tea', 'Floral Tea', 15.99),
('TEA-005', 'Ceylon Oolong Tea', 'Oolong Tea', 18.99),
('TEA-006', 'Ceylon White Tea', 'White Tea', 22.99),
('TEA-007', 'Ceylon Chai', 'Spiced Tea', 13.99),
('TEA-008', 'Ceylon Mint Tea', 'Herbal Tea', 11.99);

-- Insert sample coupons
INSERT INTO coupons (code, type, discount_value, minimum_order_amount, expiry_date, usage_limit, used_count, active, applicable_categories, stackable) VALUES
('WELCOME10', 'PERCENTAGE', 10.0, 20.0, '2099-12-31', 1000, 0, TRUE, 'Black Tea,Green Tea', FALSE),
('FLAT5', 'FIXED_AMOUNT', 5.0, 30.0, '2099-12-31', 500, 0, TRUE, '', FALSE);

-- Insert sample discounts
INSERT INTO discounts (discount_code, discount_name, discount_percentage, discount_amount, start_date, end_date, target_category, enabled) VALUES
('SUMMER20', 'Summer Sale', 20.0, NULL, '2024-06-01', '2099-08-31', 'Floral Tea', TRUE),
('FLAT3', 'Flat 3 Off', NULL, 3.0, '2024-01-01', '2099-12-31', 'Herbal Tea', TRUE);
