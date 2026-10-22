package com.productreturn.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Represents a product return request submitted by a customer.
 *
 * @author Product Return Management System
 */
public class ReturnRequest {

    private int id;
    private int orderItemId;
    private int userId;
    private String returnNumber;
    private int quantity;
    private String reason;
    private String resolutionType;
    private String description;
    private String evidenceImage;
    private String status;
    private String adminNote;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /** Transient reference to the associated order item. */
    private OrderItem orderItem;

    /** Transient reference to the requesting user. */
    private User user;

    /** Transient list of status-change history entries. */
    private List<ReturnStatusHistory> statusHistory;

    /**
     * Default constructor.
     */
    public ReturnRequest() {
    }

    /**
     * Full constructor (excluding transient fields).
     *
     * @param id             the return-request ID
     * @param orderItemId    the associated order-item ID
     * @param userId         the requesting user's ID
     * @param returnNumber   the human-readable return number
     * @param quantity       the quantity being returned
     * @param reason         the return reason
     * @param resolutionType the requested resolution (REFUND / REPLACEMENT)
     * @param description    optional detailed description
     * @param evidenceImage  optional uploaded evidence-image path
     * @param status         the current status
     * @param adminNote      optional admin note
     * @param createdAt      the creation timestamp
     * @param updatedAt      the last-update timestamp
     */
    public ReturnRequest(int id, int orderItemId, int userId, String returnNumber,
                         int quantity, String reason, String resolutionType,
                         String description, String evidenceImage, String status,
                         String adminNote, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.orderItemId = orderItemId;
        this.userId = userId;
        this.returnNumber = returnNumber;
        this.quantity = quantity;
        this.reason = reason;
        this.resolutionType = resolutionType;
        this.description = description;
        this.evidenceImage = evidenceImage;
        this.status = status;
        this.adminNote = adminNote;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReturnNumber() {
        return returnNumber;
    }

    public void setReturnNumber(String returnNumber) {
        this.returnNumber = returnNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResolutionType() {
        return resolutionType;
    }

    public void setResolutionType(String resolutionType) {
        this.resolutionType = resolutionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvidenceImage() {
        return evidenceImage;
    }

    public void setEvidenceImage(String evidenceImage) {
        this.evidenceImage = evidenceImage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ReturnStatusHistory> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<ReturnStatusHistory> statusHistory) {
        this.statusHistory = statusHistory;
    }

    @Override
    public String toString() {
        return "ReturnRequest{" +
                "id=" + id +
                ", orderItemId=" + orderItemId +
                ", userId=" + userId +
                ", returnNumber='" + returnNumber + '\'' +
                ", quantity=" + quantity +
                ", reason='" + reason + '\'' +
                ", resolutionType='" + resolutionType + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
