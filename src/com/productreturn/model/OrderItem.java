package com.productreturn.model;

import java.math.BigDecimal;

/**
 * Represents a single line item within an order.
 *
 * @author Product Return Management System
 */
public class OrderItem {

    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    /** Transient reference to the associated product. */
    private Product product;

    /** Transient reference to an existing return request for this item. */
    private ReturnRequest returnRequest;

    /**
     * Default constructor.
     */
    public OrderItem() {
    }

    /**
     * Full constructor (excluding transient fields).
     *
     * @param id        the order-item ID
     * @param orderId   the parent order ID
     * @param productId the product ID
     * @param quantity  the quantity ordered
     * @param unitPrice the unit price at time of purchase
     * @param subtotal  the line-item subtotal
     */
    public OrderItem(int id, int orderId, int productId, int quantity,
                     BigDecimal unitPrice, BigDecimal subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ReturnRequest getReturnRequest() {
        return returnRequest;
    }

    public void setReturnRequest(ReturnRequest returnRequest) {
        this.returnRequest = returnRequest;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}
