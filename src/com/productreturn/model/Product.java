package com.productreturn.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a product in the catalogue.
 *
 * @author Product Return Management System
 */
public class Product {

    private int id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private String category;
    private String imageUrl;
    private int stockQuantity;
    private boolean active;
    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public Product() {
    }

    /**
     * Full constructor.
     *
     * @param id            the product ID
     * @param name          the product name
     * @param sku           the stock keeping unit
     * @param description   the product description
     * @param price         the unit price
     * @param category      the product category
     * @param imageUrl      the URL of the product image
     * @param stockQuantity the current stock quantity
     * @param active        whether the product is active
     * @param createdAt     the creation timestamp
     */
    public Product(int id, String name, String sku, String description,
                   BigDecimal price, String category, String imageUrl,
                   int stockQuantity, boolean active, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.active = active;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", active=" + active +
                '}';
    }
}
