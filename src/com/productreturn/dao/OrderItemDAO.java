package com.productreturn.dao;

import com.productreturn.model.OrderItem;
import com.productreturn.model.Product;
import com.productreturn.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    public OrderItem findById(int id) {
        String sql = "SELECT oi.*, p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
                     "p.category AS product_category, p.description AS product_desc, p.price AS product_price " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithProduct(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderItem> findByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT oi.*, p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
                     "p.category AS product_category, p.description AS product_desc, p.price AS product_price " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ? ORDER BY oi.id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithProduct(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getReturnedQuantity(int orderItemId) {
        String sql = "SELECT COALESCE(SUM(quantity), 0) FROM return_requests " +
                     "WHERE order_item_id = ? AND status <> 'REJECTED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            ps.setBigDecimal(5, item.getSubtotal());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        item.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private OrderItem mapRowWithProduct(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));

        Product p = new Product();
        p.setId(rs.getInt("product_id"));
        p.setName(rs.getString("product_name"));
        p.setSku(rs.getString("product_sku"));
        p.setImageUrl(rs.getString("product_image"));
        p.setCategory(rs.getString("product_category"));
        p.setDescription(rs.getString("product_desc"));
        p.setPrice(rs.getBigDecimal("product_price"));
        item.setProduct(p);

        return item;
    }
}
