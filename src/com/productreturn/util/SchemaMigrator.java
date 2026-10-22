package com.productreturn.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Lightweight, idempotent schema migrator.
 * <p>
 * Runs automatically at application startup (see
 * {@link com.productreturn.listener.AppInitListener}) and can also be run manually:
 * {@code java -cp "web/WEB-INF/lib/*;build/classes" com.productreturn.util.SchemaMigrator}.
 * Every step checks the current schema first, so running it repeatedly is safe.
 * </p>
 */
public final class SchemaMigrator {

    private SchemaMigrator() {
    }

    public static void main(String[] args) throws SQLException {
        migrate();
    }

    /** Applies all pending migrations. */
    public static void migrate() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            // Migration 001: shopping cart + checkout fields
            try (Statement st = conn.createStatement()) {
                st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS cart_items (" +
                    "  id INT AUTO_INCREMENT PRIMARY KEY," +
                    "  user_id INT NOT NULL," +
                    "  product_id INT NOT NULL," +
                    "  quantity INT NOT NULL CHECK (quantity > 0)," +
                    "  added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                    "  UNIQUE KEY uq_cart_user_product (user_id, product_id)," +
                    "  CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE," +
                    "  CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB");
            }

            addColumnIfMissing(conn, "orders", "payment_method",
                    "ENUM('COD','ONLINE_DEMO') NOT NULL DEFAULT 'COD'");
            boolean addedPaymentStatus = addColumnIfMissing(conn, "orders", "payment_status",
                    "ENUM('PENDING','PAID','VOID') NOT NULL DEFAULT 'PENDING'");
            addColumnIfMissing(conn, "orders", "shipping_fee", "DECIMAL(10,2) NOT NULL DEFAULT 0.00");
            addColumnIfMissing(conn, "orders", "shipped_date", "DATE NULL DEFAULT NULL");
            addColumnIfMissing(conn, "orders", "cancelled_at", "TIMESTAMP NULL DEFAULT NULL");

            if (addedPaymentStatus) {
                // Back-fill: pre-existing delivered (COD) orders are considered paid.
                try (Statement st = conn.createStatement()) {
                    st.executeUpdate("UPDATE orders SET payment_status = 'PAID' WHERE status = 'DELIVERED'");
                    st.executeUpdate("UPDATE orders SET payment_status = 'VOID' WHERE status = 'CANCELLED'");
                }
            }
            System.out.println("[SchemaMigrator] Schema is up to date.");
        }
    }

    private static boolean addColumnIfMissing(Connection conn, String table, String column, String definition)
            throws SQLException {
        String check = "SELECT COUNT(*) FROM information_schema.COLUMNS " +
                       "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setString(1, table);
            ps.setString(2, column);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false;
                }
            }
        }
        // Identifiers come from constants in this class only, never from user input.
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("ALTER TABLE " + table + " ADD COLUMN " + column + " " + definition);
        }
        System.out.println("[SchemaMigrator] Added column " + table + "." + column);
        return true;
    }
}
