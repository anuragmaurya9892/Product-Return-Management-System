package com.productreturn.util;

import com.productreturn.dao.UserDAO;
import com.productreturn.model.User;

import java.io.Console;
import java.util.Scanner;

/**
 * Standalone setup utility for creating or resetting an administrator account.
 * Run this from the command line after deploying the schema to create an admin user
 * without committing credentials to version control.
 */
public class AdminSetup {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  Product Return Management System - Admin Setup ");
        System.out.println("=================================================");

        Scanner scanner = new Scanner(System.in);
        Console console = System.console();

        System.out.print("Enter Admin Username [default: admin]: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            username = "admin";
        }

        System.out.print("Enter Admin Email [default: admin@returnhub.local]: ");
        String email = scanner.nextLine().trim();
        if (email.isEmpty()) {
            email = "admin@returnhub.local";
        }

        System.out.print("Enter Admin Full Name [default: System Administrator]: ");
        String fullName = scanner.nextLine().trim();
        if (fullName.isEmpty()) {
            fullName = "System Administrator";
        }

        String password;
        if (console != null) {
            char[] passArray = console.readPassword("Enter Admin Password (min 8 characters): ");
            password = new String(passArray);
        } else {
            System.out.print("Enter Admin Password (min 8 characters): ");
            password = scanner.nextLine();
        }

        if (password.length() < 8) {
            System.err.println("Error: Password must be at least 8 characters long.");
            System.exit(1);
        }

        System.out.println("\nInitializing database connection...");
        DBConnection.init();

        UserDAO userDAO = new UserDAO();
        User existingUser = userDAO.findByUsername(username);

        String hashedPassword = SecurityUtil.hashPassword(password);

        if (existingUser != null) {
            System.out.println("User '" + username + "' already exists. Updating to ADMIN role with new password...");
            existingUser.setPasswordHash(hashedPassword);
            existingUser.setFullName(fullName);
            existingUser.setEmail(email);
            existingUser.setRole("ADMIN");
            // Direct SQL update for full admin fields
            String sql = "UPDATE users SET email = ?, password_hash = ?, full_name = ?, role = 'ADMIN' WHERE id = ?";
            try (java.sql.Connection conn = DBConnection.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email);
                ps.setString(2, hashedPassword);
                ps.setString(3, fullName);
                ps.setInt(4, existingUser.getId());
                ps.executeUpdate();
                System.out.println("SUCCESS: Admin account updated successfully!");
            } catch (Exception e) {
                System.err.println("Database error: " + e.getMessage());
            }
        } else {
            User admin = new User();
            admin.setUsername(username);
            admin.setEmail(email);
            admin.setFullName(fullName);
            admin.setPasswordHash(hashedPassword);
            admin.setRole("ADMIN");
            admin.setPhone("0000000000");
            admin.setAddress("System Operations HQ");

            int id = userDAO.create(admin);
            if (id > 0) {
                System.out.println("SUCCESS: Administrator account created successfully with ID: " + id);
            } else {
                System.err.println("Failed to create administrator account. Check database connection.");
            }
        }
    }
}
