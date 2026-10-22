package com.productreturn.model;

import java.sql.Timestamp;

/**
 * Represents a user in the system.
 * Users can have roles of {@code CUSTOMER} or {@code ADMIN}.
 *
 * @author Product Return Management System
 */
public class User {

    private int id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private String address;
    private String role;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /**
     * Default constructor.
     */
    public User() {
    }

    /**
     * Full constructor.
     *
     * @param id           the user ID
     * @param username     the username
     * @param email        the email address
     * @param passwordHash the BCrypt password hash
     * @param fullName     the user's full name
     * @param phone        the phone number
     * @param address      the mailing address
     * @param role         the role (CUSTOMER or ADMIN)
     * @param createdAt    the creation timestamp
     * @param updatedAt    the last update timestamp
     */
    public User(int id, String username, String email, String passwordHash,
                String fullName, String phone, String address, String role,
                Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.role = role;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
