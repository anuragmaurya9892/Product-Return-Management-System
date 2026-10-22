package com.productreturn.dao;

import com.productreturn.model.CartItem;
import com.productreturn.model.Product;
import com.productreturn.util.DBConnection;
import com.productreturn.util.PricingUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistent shopping cart (survives logout and works across devices).
 */
public class CartDAO {

    public List<CartItem> findByUserId(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT ci.*, p.name, p.sku, p.price, p.category, p.image_url, p.stock_quantity, p.active " +
                     "FROM cart_items ci JOIN products p ON ci.product_id = p.id " +
                     "WHERE ci.user_id = ? ORDER BY ci.added_at DESC, ci.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem();
                    ci.setId(rs.getInt("id"));
                    ci.setUserId(rs.getInt("user_id"));
                    ci.setProductId(rs.getInt("product_id"));
                    ci.setQuantity(rs.getInt("quantity"));
                    ci.setAddedAt(rs.getTimestamp("added_at"));

                    Product p = new Product();
                    p.setId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setSku(rs.getString("sku"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setCategory(rs.getString("category"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setStockQuantity(rs.getInt("stock_quantity"));
                    p.setActive(rs.getBoolean("active"));
                    ci.setProduct(p);
                    list.add(ci);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** Total units in the user's cart. */
    public int countUnits(int userId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** Quantity of one product currently in the cart (0 if absent). */
    public int quantityOf(int userId, int productId) {
        String sql = "SELECT quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Adds {@code qty} units (or increments an existing line). The resulting quantity is
     * capped by the caller; this method simply writes it.
     */
    public boolean setQuantity(int userId, int productId, int qty) {
        if (qty <= 0) {
            return remove(userId, productId);
        }
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = VALUES(quantity)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean remove(int userId, int productId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean clear(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Sum of line totals using current product prices. */
    public static BigDecimal subtotal(List<CartItem> items) {
        BigDecimal sum = BigDecimal.ZERO;
        for (CartItem ci : items) {
            if (ci.getProduct() != null) {
                sum = sum.add(PricingUtil.lineTotal(ci.getProduct().getPrice(), ci.getQuantity()));
            }
        }
        return sum.setScale(2, java.math.RoundingMode.HALF_UP);
    }
}
