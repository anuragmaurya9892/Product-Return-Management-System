-- ============================================================
-- Product Return Management System - Database Schema
-- Database: product_return_db
-- MySQL 8.0+
-- ============================================================

CREATE DATABASE IF NOT EXISTS product_return_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE product_return_db;

-- Drop existing tables in reverse dependency order for a clean reset
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS refunds;
DROP TABLE IF EXISTS return_status_history;
DROP TABLE IF EXISTS return_requests;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 1. USERS TABLE
-- ============================================================
CREATE TABLE users (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(50)     NOT NULL UNIQUE,
    email           VARCHAR(100)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    full_name       VARCHAR(100)    NOT NULL,
    phone           VARCHAR(20),
    address         TEXT,
    role            ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_role (role),
    INDEX idx_users_email (email)
) ENGINE=InnoDB;

-- ============================================================
-- 2. PRODUCTS TABLE
-- ============================================================
CREATE TABLE products (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(200)    NOT NULL,
    sku             VARCHAR(50)     NOT NULL UNIQUE,
    description     TEXT,
    price           DECIMAL(10,2)   NOT NULL CHECK (price >= 0),
    category        VARCHAR(100),
    image_url       VARCHAR(500),
    stock_quantity  INT             NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_products_category (category),
    INDEX idx_products_active (active),
    INDEX idx_products_name (name)
) ENGINE=InnoDB;

-- ============================================================
-- 3. ORDERS TABLE
-- ============================================================
CREATE TABLE orders (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT             NOT NULL,
    order_number     VARCHAR(30)     NOT NULL UNIQUE,
    total_amount     DECIMAL(10,2)   NOT NULL CHECK (total_amount >= 0),
    status           ENUM('PLACED', 'SHIPPED', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PLACED',
    order_date       DATE            NOT NULL,
    delivered_date   DATE            DEFAULT NULL,
    shipping_address TEXT            NOT NULL,
    created_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_orders_user_id (user_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_order_date (order_date),
    INDEX idx_orders_order_number (order_number)
) ENGINE=InnoDB;

-- ============================================================
-- 4. ORDER ITEMS TABLE
-- ============================================================
CREATE TABLE order_items (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    order_id        INT             NOT NULL,
    product_id      INT             NOT NULL,
    quantity        INT             NOT NULL CHECK (quantity > 0),
    unit_price      DECIMAL(10,2)   NOT NULL CHECK (unit_price >= 0),
    subtotal        DECIMAL(10,2)   NOT NULL CHECK (subtotal >= 0),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_order_items_order_id (order_id),
    INDEX idx_order_items_product_id (product_id)
) ENGINE=InnoDB;

-- ============================================================
-- 5. RETURN REQUESTS TABLE
-- ============================================================
CREATE TABLE return_requests (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    order_item_id    INT             NOT NULL,
    user_id          INT             NOT NULL,
    return_number    VARCHAR(30)     NOT NULL UNIQUE,
    quantity         INT             NOT NULL CHECK (quantity > 0),
    reason           ENUM('DAMAGED', 'DEFECTIVE', 'WRONG_ITEM', 'INCORRECT_SIZE', 'NOT_NEEDED') NOT NULL,
    resolution_type  ENUM('REFUND', 'REPLACEMENT') NOT NULL,
    description      TEXT,
    evidence_image   VARCHAR(500),
    status           ENUM('PENDING', 'APPROVED', 'REJECTED', 'ITEM_RECEIVED', 'REFUND_PROCESSED', 'REPLACEMENT_SHIPPED')
                     NOT NULL DEFAULT 'PENDING',
    admin_note       TEXT,
    created_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_return_order_item FOREIGN KEY (order_item_id) REFERENCES order_items(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_return_user FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_return_user_id (user_id),
    INDEX idx_return_status (status),
    INDEX idx_return_order_item_id (order_item_id),
    INDEX idx_return_created_at (created_at),
    INDEX idx_return_return_number (return_number)
) ENGINE=InnoDB;

-- ============================================================
-- 6. RETURN STATUS HISTORY TABLE
-- ============================================================
CREATE TABLE return_status_history (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    return_request_id   INT             NOT NULL,
    old_status          ENUM('PENDING', 'APPROVED', 'REJECTED', 'ITEM_RECEIVED', 'REFUND_PROCESSED', 'REPLACEMENT_SHIPPED'),
    new_status          ENUM('PENDING', 'APPROVED', 'REJECTED', 'ITEM_RECEIVED', 'REFUND_PROCESSED', 'REPLACEMENT_SHIPPED') NOT NULL,
    changed_by          VARCHAR(100)    NOT NULL,
    note                TEXT,
    changed_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_status_history_return FOREIGN KEY (return_request_id) REFERENCES return_requests(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_status_history_return_id (return_request_id),
    INDEX idx_status_history_changed_at (changed_at)
) ENGINE=InnoDB;

-- ============================================================
-- 7. REFUNDS TABLE
-- ============================================================
CREATE TABLE refunds (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    return_request_id   INT             NOT NULL UNIQUE,
    amount              DECIMAL(10,2)   NOT NULL CHECK (amount >= 0),
    status              ENUM('PENDING', 'PROCESSED') NOT NULL DEFAULT 'PENDING',
    processed_at        TIMESTAMP       NULL DEFAULT NULL,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_refunds_return FOREIGN KEY (return_request_id) REFERENCES return_requests(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_refunds_status (status)
) ENGINE=InnoDB;
