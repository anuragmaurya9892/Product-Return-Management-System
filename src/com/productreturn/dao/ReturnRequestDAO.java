package com.productreturn.dao;

import com.productreturn.model.*;
import com.productreturn.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnRequestDAO {

    public ReturnRequest findById(int id) {
        String sql = "SELECT r.*, u.username, u.full_name, u.email, " +
                     "oi.order_id, oi.product_id, oi.quantity AS ordered_qty, oi.unit_price, oi.subtotal, " +
                     "p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
                     "o.order_number, o.delivered_date " +
                     "FROM return_requests r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN order_items oi ON r.order_item_id = oi.id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "WHERE r.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReturnRequest req = mapRowWithDetails(rs);
                    ReturnStatusHistoryDAO historyDAO = new ReturnStatusHistoryDAO();
                    req.setStatusHistory(historyDAO.findByReturnRequestId(req.getId()));
                    return req;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ReturnRequest findByReturnNumber(String returnNumber) {
        String sql = "SELECT r.*, u.username, u.full_name, u.email, " +
                     "oi.order_id, oi.product_id, oi.quantity AS ordered_qty, oi.unit_price, oi.subtotal, " +
                     "p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
                     "o.order_number, o.delivered_date " +
                     "FROM return_requests r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN order_items oi ON r.order_item_id = oi.id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "WHERE r.return_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, returnNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReturnRequest req = mapRowWithDetails(rs);
                    ReturnStatusHistoryDAO historyDAO = new ReturnStatusHistoryDAO();
                    req.setStatusHistory(historyDAO.findByReturnRequestId(req.getId()));
                    return req;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ReturnRequest> findByUserId(int userId) {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.full_name, u.email, " +
                     "oi.order_id, oi.product_id, oi.quantity AS ordered_qty, oi.unit_price, oi.subtotal, " +
                     "p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
                     "o.order_number, o.delivered_date " +
                     "FROM return_requests r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN order_items oi ON r.order_item_id = oi.id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "WHERE r.user_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ReturnRequest> findAll() {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username, u.full_name, u.email, " +
                     "oi.order_id, oi.product_id, oi.quantity AS ordered_qty, oi.unit_price, oi.subtotal, " +
                     "p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
                     "o.order_number, o.delivered_date " +
                     "FROM return_requests r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN order_items oi ON r.order_item_id = oi.id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "JOIN orders o ON oi.order_id = o.id " +
                     "ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowWithDetails(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ReturnRequest> findByOrderItemId(int orderItemId) {
        List<ReturnRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM return_requests WHERE order_item_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowSimple(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean hasActiveReturn(int orderItemId) {
        String sql = "SELECT COUNT(*) FROM return_requests WHERE order_item_id = ? AND status <> 'REJECTED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderItemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ReturnRequest> searchAndFilter(String search, String status, String resolution, String dateFrom, String dateTo) {
        List<ReturnRequest> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, u.username, u.full_name, u.email, " +
            "oi.order_id, oi.product_id, oi.quantity AS ordered_qty, oi.unit_price, oi.subtotal, " +
            "p.name AS product_name, p.sku AS product_sku, p.image_url AS product_image, " +
            "o.order_number, o.delivered_date " +
            "FROM return_requests r " +
            "JOIN users u ON r.user_id = u.id " +
            "JOIN order_items oi ON r.order_item_id = oi.id " +
            "JOIN products p ON oi.product_id = p.id " +
            "JOIN orders o ON oi.order_id = o.id " +
            "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND (r.return_number LIKE ? OR o.order_number LIKE ? OR u.username LIKE ? OR u.full_name LIKE ? OR p.name LIKE ?)");
            String term = "%" + search.trim() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (status != null && !status.trim().isEmpty() && !"ALL".equalsIgnoreCase(status)) {
            sql.append(" AND r.status = ?");
            params.add(status.trim());
        }

        if (resolution != null && !resolution.trim().isEmpty() && !"ALL".equalsIgnoreCase(resolution)) {
            sql.append(" AND r.resolution_type = ?");
            params.add(resolution.trim());
        }

        if (dateFrom != null && !dateFrom.trim().isEmpty()) {
            sql.append(" AND DATE(r.created_at) >= ?");
            params.add(Date.valueOf(dateFrom.trim()));
        }

        if (dateTo != null && !dateTo.trim().isEmpty()) {
            sql.append(" AND DATE(r.created_at) <= ?");
            params.add(Date.valueOf(dateTo.trim()));
        }

        sql.append(" ORDER BY r.created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithDetails(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int create(ReturnRequest req) {
        String sql = "INSERT INTO return_requests (order_item_id, user_id, return_number, quantity, reason, " +
                     "resolution_type, description, evidence_image, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int returnId = -1;
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, req.getOrderItemId());
                ps.setInt(2, req.getUserId());
                ps.setString(3, req.getReturnNumber());
                ps.setInt(4, req.getQuantity());
                ps.setString(5, req.getReason());
                ps.setString(6, req.getResolutionType());
                ps.setString(7, req.getDescription());
                ps.setString(8, req.getEvidenceImage());
                ps.setString(9, req.getStatus() != null ? req.getStatus() : "PENDING");

                int affected = ps.executeUpdate();
                if (affected > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            returnId = rs.getInt(1);
                            req.setId(returnId);
                        }
                    }
                }
            }

            if (returnId > 0) {
                // Record initial status history
                ReturnStatusHistory h = new ReturnStatusHistory();
                h.setReturnRequestId(returnId);
                h.setOldStatus(null);
                h.setNewStatus(req.getStatus() != null ? req.getStatus() : "PENDING");
                h.setChangedBy("Customer (ID: " + req.getUserId() + ")");
                h.setNote("Return request submitted");

                ReturnStatusHistoryDAO historyDAO = new ReturnStatusHistoryDAO();
                historyDAO.create(conn, h);

                conn.commit();
                return returnId;
            } else {
                conn.rollback();
            }
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return -1;
    }

    public boolean updateStatus(int returnId, String newStatus, String adminNote, String changedBy) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // Fetch current request
            String selectSql = "SELECT * FROM return_requests WHERE id = ? FOR UPDATE";
            String oldStatus = null;
            String resolutionType = null;
            int orderItemId = 0;
            int quantity = 0;

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, returnId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        oldStatus = rs.getString("status");
                        resolutionType = rs.getString("resolution_type");
                        orderItemId = rs.getInt("order_item_id");
                        quantity = rs.getInt("quantity");
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Update return request
            String updateSql = "UPDATE return_requests SET status = ?, admin_note = ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, newStatus);
                ps.setString(2, adminNote);
                ps.setInt(3, returnId);
                ps.executeUpdate();
            }

            // Record status history
            ReturnStatusHistory h = new ReturnStatusHistory();
            h.setReturnRequestId(returnId);
            h.setOldStatus(oldStatus);
            h.setNewStatus(newStatus);
            h.setChangedBy(changedBy);
            h.setNote(adminNote);

            ReturnStatusHistoryDAO historyDAO = new ReturnStatusHistoryDAO();
            historyDAO.create(conn, h);

            // Handle refund workflow
            RefundDAO refundDAO = new RefundDAO();
            if ("REFUND".equalsIgnoreCase(resolutionType)) {
                if ("APPROVED".equalsIgnoreCase(newStatus)) {
                    // Calculate refund amount from order item
                    BigDecimal refundAmount = BigDecimal.ZERO;
                    String itemSql = "SELECT unit_price FROM order_items WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                        ps.setInt(1, orderItemId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                BigDecimal unitPrice = rs.getBigDecimal("unit_price");
                                refundAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
                            }
                        }
                    }

                    Refund existingRefund = refundDAO.findByReturnRequestId(returnId);
                    if (existingRefund == null) {
                        Refund r = new Refund();
                        r.setReturnRequestId(returnId);
                        r.setAmount(refundAmount);
                        r.setStatus("PENDING");
                        refundDAO.create(conn, r);
                    }
                } else if ("REFUND_PROCESSED".equalsIgnoreCase(newStatus)) {
                    refundDAO.updateStatus(conn, returnId, "PROCESSED");
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return false;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM return_requests";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM return_requests WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
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

    public int countByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM return_requests WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
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

    public int countByUserIdAndStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM return_requests WHERE user_id = ? AND status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, status);
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

    private ReturnRequest mapRowSimple(ResultSet rs) throws SQLException {
        ReturnRequest r = new ReturnRequest();
        r.setId(rs.getInt("id"));
        r.setOrderItemId(rs.getInt("order_item_id"));
        r.setUserId(rs.getInt("user_id"));
        r.setReturnNumber(rs.getString("return_number"));
        r.setQuantity(rs.getInt("quantity"));
        r.setReason(rs.getString("reason"));
        r.setResolutionType(rs.getString("resolution_type"));
        r.setDescription(rs.getString("description"));
        r.setEvidenceImage(rs.getString("evidence_image"));
        r.setStatus(rs.getString("status"));
        r.setAdminNote(rs.getString("admin_note"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        r.setUpdatedAt(rs.getTimestamp("updated_at"));
        return r;
    }

    private ReturnRequest mapRowWithDetails(ResultSet rs) throws SQLException {
        ReturnRequest r = mapRowSimple(rs);

        User u = new User();
        u.setId(r.getUserId());
        u.setUsername(rs.getString("username"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        r.setUser(u);

        OrderItem oi = new OrderItem();
        oi.setId(r.getOrderItemId());
        oi.setOrderId(rs.getInt("order_id"));
        oi.setProductId(rs.getInt("product_id"));
        oi.setQuantity(rs.getInt("ordered_qty"));
        oi.setUnitPrice(rs.getBigDecimal("unit_price"));
        oi.setSubtotal(rs.getBigDecimal("subtotal"));

        Product p = new Product();
        p.setId(rs.getInt("product_id"));
        p.setName(rs.getString("product_name"));
        p.setSku(rs.getString("product_sku"));
        p.setImageUrl(rs.getString("product_image"));
        oi.setProduct(p);

        r.setOrderItem(oi);
        return r;
    }
}
