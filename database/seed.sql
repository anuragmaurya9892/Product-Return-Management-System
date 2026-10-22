-- ============================================================
-- Product Return Management System - Seed Data
-- Run AFTER schema.sql
-- ============================================================
-- NOTE: This file does NOT create an admin user with a hardcoded password.
-- See database/README.md for instructions on creating the admin account
-- securely using the provided setup procedure.
-- ============================================================

USE product_return_db;

-- ============================================================
-- STORED PROCEDURE: Create Admin User
-- Usage: CALL create_admin_user('admin', 'admin@company.com', '<bcrypt_hash>', 'System Administrator');
-- Generate a BCrypt hash using the application's setup utility or an online BCrypt generator.
-- ============================================================
DELIMITER //
CREATE PROCEDURE IF NOT EXISTS create_admin_user(
    IN p_username VARCHAR(50),
    IN p_email VARCHAR(100),
    IN p_password_hash VARCHAR(255),
    IN p_full_name VARCHAR(100)
)
BEGIN
    INSERT INTO users (username, email, password_hash, full_name, role)
    VALUES (p_username, p_email, p_password_hash, p_full_name, 'ADMIN')
    ON DUPLICATE KEY UPDATE
        email = p_email,
        password_hash = p_password_hash,
        full_name = p_full_name,
        role = 'ADMIN';
END //
DELIMITER ;

-- ============================================================
-- DEMO CUSTOMER (password will be set via setup utility)
-- For local demo: password = "customer123"
-- BCrypt hash of "customer123" (cost 12):
-- ============================================================
INSERT INTO users (username, email, password_hash, full_name, phone, address, role) VALUES
('john_doe', 'john@example.com', '$2a$12$LJ3m4ys3LkFqMmOyKMRWceYpJP.ghn/rkuRhrgSRqHSIK4Mxgeq6G', 'John Doe', '9876543210', '123 Main Street, Mumbai, Maharashtra 400001', 'CUSTOMER'),
('jane_smith', 'jane@example.com', '$2a$12$LJ3m4ys3LkFqMmOyKMRWceYpJP.ghn/rkuRhrgSRqHSIK4Mxgeq6G', 'Jane Smith', '9876543211', '456 Park Avenue, Delhi, Delhi 110001', 'CUSTOMER'),
('raj_patel', 'raj@example.com', '$2a$12$LJ3m4ys3LkFqMmOyKMRWceYpJP.ghn/rkuRhrgSRqHSIK4Mxgeq6G', 'Raj Patel', '9876543212', '789 MG Road, Bangalore, Karnataka 560001', 'CUSTOMER');

-- ============================================================
-- PRODUCTS - Electronics & Accessories
-- ============================================================
INSERT INTO products (name, sku, description, price, category, image_url, stock_quantity, active) VALUES
('Wireless Bluetooth Headphones', 'ELEC-WBH-001', 'Premium over-ear wireless headphones with noise cancellation, 30-hour battery life, and comfortable memory foam ear cups.', 2499.00, 'Electronics', '/images/products/headphones.jpg', 150, TRUE),
('USB-C Fast Charging Cable (2m)', 'ELEC-UCB-002', 'Durable braided USB-C to USB-C cable with 100W PD fast charging support. Compatible with laptops, tablets, and phones.', 499.00, 'Electronics', '/images/products/usb-cable.jpg', 500, TRUE),
('Portable Power Bank 20000mAh', 'ELEC-PPB-003', 'Slim portable charger with dual USB-A and USB-C ports. LED display shows remaining battery percentage.', 1799.00, 'Electronics', '/images/products/powerbank.jpg', 200, TRUE),
('Mechanical Gaming Keyboard', 'ELEC-MGK-004', 'RGB backlit mechanical keyboard with Cherry MX Blue switches, aluminum frame, and programmable macro keys.', 4999.00, 'Electronics', '/images/products/keyboard.jpg', 80, TRUE),
('Wireless Ergonomic Mouse', 'ELEC-WEM-005', 'Vertical ergonomic mouse with adjustable DPI (800-4000), silent clicks, and rechargeable battery.', 1299.00, 'Electronics', '/images/products/mouse.jpg', 120, TRUE),

-- Clothing
('Men''s Cotton Casual T-Shirt', 'CLTH-MCT-001', '100% organic cotton crew neck t-shirt. Pre-shrunk, comfortable fit. Available in multiple colors.', 699.00, 'Clothing', '/images/products/tshirt.jpg', 300, TRUE),
('Women''s Running Shoes', 'CLTH-WRS-002', 'Lightweight breathable mesh running shoes with cushioned sole and arch support. Ideal for daily running.', 3499.00, 'Clothing', '/images/products/shoes.jpg', 100, TRUE),
('Unisex Waterproof Jacket', 'CLTH-UWJ-003', 'Seam-sealed waterproof jacket with adjustable hood, zippered pockets, and reflective strips for visibility.', 2999.00, 'Clothing', '/images/products/jacket.jpg', 75, TRUE),

-- Home & Kitchen
('Stainless Steel Water Bottle (1L)', 'HOME-SSW-001', 'Double-wall vacuum insulated water bottle. Keeps drinks cold for 24 hours or hot for 12 hours. BPA-free.', 899.00, 'Home & Kitchen', '/images/products/bottle.jpg', 250, TRUE),
('Non-Stick Frying Pan (28cm)', 'HOME-NFP-002', 'PFOA-free ceramic coated frying pan with bakelite handle. Compatible with gas and induction cooktops.', 1499.00, 'Home & Kitchen', '/images/products/pan.jpg', 60, TRUE),
('Smart LED Desk Lamp', 'HOME-SDL-003', 'Touch-controlled LED desk lamp with 5 brightness levels, 3 color temperatures, USB charging port, and 60-minute timer.', 1999.00, 'Home & Kitchen', '/images/products/lamp.jpg', 90, TRUE),

-- Books
('Clean Code by Robert C. Martin', 'BOOK-CCR-001', 'A handbook of agile software craftsmanship. Learn to write clean, maintainable, and efficient code.', 599.00, 'Books', '/images/products/cleancode.jpg', 40, TRUE),
('The Pragmatic Programmer', 'BOOK-TPP-002', 'Classic guide covering software engineering best practices, from career development to architecture patterns.', 649.00, 'Books', '/images/products/pragmatic.jpg', 35, TRUE);

-- ============================================================
-- ORDERS for john_doe (user_id = 1)
-- ============================================================
INSERT INTO orders (user_id, order_number, total_amount, status, order_date, delivered_date, shipping_address) VALUES
(1, 'ORD-20261001-0001', 4298.00, 'DELIVERED', '2026-10-01', '2026-10-05', '123 Main Street, Mumbai, Maharashtra 400001'),
(1, 'ORD-20261010-0002', 3498.00, 'DELIVERED', '2026-10-10', '2026-10-14', '123 Main Street, Mumbai, Maharashtra 400001'),
(1, 'ORD-20261019-0003', 1799.00, 'DELIVERED', '2026-10-19', '2026-10-21', '123 Main Street, Mumbai, Maharashtra 400001'),
(1, 'ORD-20261022-0004', 699.00,  'SHIPPED',   '2026-10-22', NULL,         '123 Main Street, Mumbai, Maharashtra 400001');

-- ORDERS for jane_smith (user_id = 2)
INSERT INTO orders (user_id, order_number, total_amount, status, order_date, delivered_date, shipping_address) VALUES
(2, 'ORD-20261005-0005', 6498.00, 'DELIVERED', '2026-10-05', '2026-10-09', '456 Park Avenue, Delhi, Delhi 110001'),
(2, 'ORD-20261015-0006', 2498.00, 'DELIVERED', '2026-10-15', '2026-10-19', '456 Park Avenue, Delhi, Delhi 110001');

-- ORDERS for raj_patel (user_id = 3)
INSERT INTO orders (user_id, order_number, total_amount, status, order_date, delivered_date, shipping_address) VALUES
(3, 'ORD-20261008-0007', 5898.00, 'DELIVERED', '2026-10-08', '2026-10-12', '789 MG Road, Bangalore, Karnataka 560001');

-- ============================================================
-- ORDER ITEMS
-- ============================================================
-- Order 1 (john_doe): Headphones + Power Bank
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(1, 1, 1, 2499.00, 2499.00),
(1, 3, 1, 1799.00, 1799.00);

-- Order 2 (john_doe): Shoes
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(2, 7, 1, 3499.00, 3499.00);

-- Order 3 (john_doe): Power Bank (another order, recently delivered)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(3, 3, 1, 1799.00, 1799.00);

-- Order 4 (john_doe): T-Shirt (still shipped, not delivered yet)
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(4, 6, 1, 699.00, 699.00);

-- Order 5 (jane_smith): Keyboard + Mouse + Water Bottle
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(5, 4, 1, 4999.00, 4999.00),
(5, 5, 1, 1299.00, 1299.00),
(5, 9, 1, 899.00, 899.00);

-- Order 6 (jane_smith): USB Cable x2 + Pan
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(6, 2, 2, 499.00, 998.00),
(6, 10, 1, 1499.00, 1499.00);

-- Order 7 (raj_patel): Jacket + Lamp + Clean Code + Pragmatic Programmer
INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES
(7, 8, 1, 2999.00, 2999.00),
(7, 11, 1, 1999.00, 1999.00),
(7, 12, 1, 599.00, 599.00);

-- ============================================================
-- SAMPLE RETURN REQUESTS
-- ============================================================
-- Return 1: john_doe returning headphones from order 1 (PENDING)
INSERT INTO return_requests (order_item_id, user_id, return_number, quantity, reason, resolution_type, description, status) VALUES
(1, 1, 'RET-20261018-0001', 1, 'DEFECTIVE', 'REPLACEMENT', 'Left ear speaker stopped working after 2 days of use. No physical damage.', 'PENDING');

-- Return 2: jane_smith returning keyboard from order 5 (APPROVED)
INSERT INTO return_requests (order_item_id, user_id, return_number, quantity, reason, resolution_type, description, status, admin_note) VALUES
(7, 2, 'RET-20261016-0002', 1, 'DAMAGED', 'REFUND', 'Product arrived with broken spacebar key and scratched surface. Box was also dented.', 'APPROVED', 'Damage confirmed from photos. Approved for full refund.');

-- Return 3: raj_patel returning jacket from order 7 (ITEM_RECEIVED)
INSERT INTO return_requests (order_item_id, user_id, return_number, quantity, reason, resolution_type, description, status, admin_note) VALUES
(11, 3, 'RET-20261015-0003', 1, 'INCORRECT_SIZE', 'REPLACEMENT', 'Ordered XL but received L size. Need correct size.', 'ITEM_RECEIVED', 'Item received at warehouse. Processing replacement.');

-- ============================================================
-- STATUS HISTORY for the sample returns
-- ============================================================
-- Return 1 history
INSERT INTO return_status_history (return_request_id, old_status, new_status, changed_by, note) VALUES
(1, NULL, 'PENDING', 'john_doe', 'Return request submitted');

-- Return 2 history
INSERT INTO return_status_history (return_request_id, old_status, new_status, changed_by, note) VALUES
(2, NULL, 'PENDING', 'jane_smith', 'Return request submitted'),
(2, 'PENDING', 'APPROVED', 'admin', 'Damage confirmed from photos. Approved for full refund.');

-- Return 3 history
INSERT INTO return_status_history (return_request_id, old_status, new_status, changed_by, note) VALUES
(3, NULL, 'PENDING', 'raj_patel', 'Return request submitted'),
(3, 'PENDING', 'APPROVED', 'admin', 'Size mismatch confirmed. Approved for replacement.'),
(3, 'APPROVED', 'ITEM_RECEIVED', 'admin', 'Item received at warehouse. Processing replacement.');

-- ============================================================
-- REFUND record for return 2 (PENDING refund)
-- ============================================================
INSERT INTO refunds (return_request_id, amount, status) VALUES
(2, 4999.00, 'PENDING');
