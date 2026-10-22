package com.productreturn.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Represents a refund associated with an approved return request.
 *
 * @author Product Return Management System
 */
public class Refund {

    private int id;
    private int returnRequestId;
    private BigDecimal amount;
    private String status;
    private Timestamp processedAt;
    private Timestamp createdAt;

    /**
     * Default constructor.
     */
    public Refund() {
    }

    /**
     * Full constructor.
     *
     * @param id              the refund ID
     * @param returnRequestId the associated return-request ID
     * @param amount          the refund amount
     * @param status          the refund status (PENDING / PROCESSED)
     * @param processedAt     the timestamp when the refund was processed (may be null)
     * @param createdAt       the creation timestamp
     */
    public Refund(int id, int returnRequestId, BigDecimal amount, String status,
                  Timestamp processedAt, Timestamp createdAt) {
        this.id = id;
        this.returnRequestId = returnRequestId;
        this.amount = amount;
        this.status = status;
        this.processedAt = processedAt;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReturnRequestId() {
        return returnRequestId;
    }

    public void setReturnRequestId(int returnRequestId) {
        this.returnRequestId = returnRequestId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Timestamp processedAt) {
        this.processedAt = processedAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "id=" + id +
                ", returnRequestId=" + returnRequestId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", processedAt=" + processedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
