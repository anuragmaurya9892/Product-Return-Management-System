package com.productreturn.model;

import java.sql.Timestamp;

/**
 * Records a single status-change event for a return request,
 * providing a full audit trail.
 *
 * @author Product Return Management System
 */
public class ReturnStatusHistory {

    private int id;
    private int returnRequestId;
    private String oldStatus;
    private String newStatus;
    private String changedBy;
    private String note;
    private Timestamp changedAt;

    /**
     * Default constructor.
     */
    public ReturnStatusHistory() {
    }

    /**
     * Full constructor.
     *
     * @param id              the history entry ID
     * @param returnRequestId the associated return-request ID
     * @param oldStatus       the previous status
     * @param newStatus       the new status
     * @param changedBy       the username of the person who made the change
     * @param note            an optional note
     * @param changedAt       the timestamp of the change
     */
    public ReturnStatusHistory(int id, int returnRequestId, String oldStatus,
                               String newStatus, String changedBy, String note,
                               Timestamp changedAt) {
        this.id = id;
        this.returnRequestId = returnRequestId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.note = note;
        this.changedAt = changedAt;
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

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    @Override
    public String toString() {
        return "ReturnStatusHistory{" +
                "id=" + id +
                ", returnRequestId=" + returnRequestId +
                ", oldStatus='" + oldStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", changedBy='" + changedBy + '\'' +
                ", changedAt=" + changedAt +
                '}';
    }
}
