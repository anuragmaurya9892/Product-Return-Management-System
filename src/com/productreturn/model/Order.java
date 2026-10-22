package com.productreturn.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a customer order.
 *
 * @author Product Return Management System
 */
public class Order {

    private int id;
    private int userId;
    private String orderNumber;
    private BigDecimal totalAmount;
    private String status;
    private Date orderDate;
    private Date deliveredDate;
    private String shippingAddress;
    private Timestamp createdAt;

    /** Transient list of order line items. */
    private List<OrderItem> items;

    /** Transient reference to the owning user. */
    private User user;

    /**
     * Default constructor.
     */
    public Order() {
    }

    /**
     * Full constructor (excluding transient fields).
     *
     * @param id              the order ID
     * @param userId          the ID of the ordering user
     * @param orderNumber     the human-readable order number
     * @param totalAmount     the total order amount
     * @param status          the order status
     * @param orderDate       the date the order was placed
     * @param deliveredDate   the date the order was delivered (may be null)
     * @param shippingAddress the shipping address
     * @param createdAt       the creation timestamp
     */
    public Order(int id, int userId, String orderNumber, BigDecimal totalAmount,
                 String status, Date orderDate, Date deliveredDate,
                 String shippingAddress, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.orderNumber = orderNumber;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
        this.deliveredDate = deliveredDate;
        this.shippingAddress = shippingAddress;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", orderNumber='" + orderNumber + '\'' +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", orderDate=" + orderDate +
                ", deliveredDate=" + deliveredDate +
                '}';
    }
}
