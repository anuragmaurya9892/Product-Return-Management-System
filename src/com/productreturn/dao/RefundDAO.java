package com.productreturn.dao;

import com.productreturn.model.Refund;
import com.productreturn.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public class RefundDAO {

    public Refund findByReturnRequestId(int returnRequestId) {
        String sql = "SELECT * FROM refunds WHERE return_request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, returnRequestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int create(Refund refund) {
        String sql = "INSERT INTO refunds (return_request_id, amount, status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, refund.getReturnRequestId());
            ps.setBigDecimal(2, refund.getAmount());
            ps.setString(3, refund.getStatus() != null ? refund.getStatus() : "PENDING");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        refund.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int create(Connection conn, Refund refund) throws SQLException {
        String sql = "INSERT INTO refunds (return_request_id, amount, status) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, refund.getReturnRequestId());
            ps.setBigDecimal(2, refund.getAmount());
            ps.setString(3, refund.getStatus() != null ? refund.getStatus() : "PENDING");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        refund.setId(id);
                        return id;
                    }
                }
            }
        }
        return -1;
    }

    public boolean updateStatus(int returnRequestId, String status) {
        String sql = "UPDATE refunds SET status = ?, processed_at = CASE WHEN ? = 'PROCESSED' THEN CURRENT_TIMESTAMP ELSE processed_at END WHERE return_request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, returnRequestId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(Connection conn, int returnRequestId, String status) throws SQLException {
        String sql = "UPDATE refunds SET status = ?, processed_at = CASE WHEN ? = 'PROCESSED' THEN CURRENT_TIMESTAMP ELSE processed_at END WHERE return_request_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, status);
            ps.setInt(3, returnRequestId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countProcessed() {
        String sql = "SELECT COUNT(*) FROM refunds WHERE status = 'PROCESSED'";
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

    public BigDecimal sumProcessed() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM refunds WHERE status = 'PROCESSED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    private Refund mapRow(ResultSet rs) throws SQLException {
        Refund r = new Refund();
        r.setId(rs.getInt("id"));
        r.setReturnRequestId(rs.getInt("return_request_id"));
        r.setAmount(rs.getBigDecimal("amount"));
        r.setStatus(rs.getString("status"));
        r.setProcessedAt(rs.getTimestamp("processed_at"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
