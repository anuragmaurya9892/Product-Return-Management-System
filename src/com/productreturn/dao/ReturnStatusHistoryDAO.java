package com.productreturn.dao;

import com.productreturn.model.ReturnStatusHistory;
import com.productreturn.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnStatusHistoryDAO {

    public List<ReturnStatusHistory> findByReturnRequestId(int returnRequestId) {
        List<ReturnStatusHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM return_status_history WHERE return_request_id = ? ORDER BY changed_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, returnRequestId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReturnStatusHistory h = new ReturnStatusHistory();
                    h.setId(rs.getInt("id"));
                    h.setReturnRequestId(rs.getInt("return_request_id"));
                    h.setOldStatus(rs.getString("old_status"));
                    h.setNewStatus(rs.getString("new_status"));
                    h.setChangedBy(rs.getString("changed_by"));
                    h.setNote(rs.getString("note"));
                    h.setChangedAt(rs.getTimestamp("changed_at"));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int create(ReturnStatusHistory h) {
        String sql = "INSERT INTO return_status_history (return_request_id, old_status, new_status, changed_by, note) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, h.getReturnRequestId());
            ps.setString(2, h.getOldStatus());
            ps.setString(3, h.getNewStatus());
            ps.setString(4, h.getChangedBy());
            ps.setString(5, h.getNote());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        h.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int create(Connection conn, ReturnStatusHistory h) throws SQLException {
        String sql = "INSERT INTO return_status_history (return_request_id, old_status, new_status, changed_by, note) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, h.getReturnRequestId());
            ps.setString(2, h.getOldStatus());
            ps.setString(3, h.getNewStatus());
            ps.setString(4, h.getChangedBy());
            ps.setString(5, h.getNote());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        h.setId(id);
                        return id;
                    }
                }
            }
        }
        return -1;
    }
}
