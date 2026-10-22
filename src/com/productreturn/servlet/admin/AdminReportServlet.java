package com.productreturn.servlet.admin;

import com.productreturn.dao.RefundDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminReportServlet extends HttpServlet {

    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();
    private final RefundDAO refundDAO = new RefundDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Integer> returnsByReason = new LinkedHashMap<>();
        Map<String, Integer> returnsByStatus = new LinkedHashMap<>();
        Map<String, Integer> returnsByResolution = new LinkedHashMap<>();

        String reasonSql = "SELECT reason, COUNT(*) as cnt FROM return_requests GROUP BY reason ORDER BY cnt DESC";
        String statusSql = "SELECT status, COUNT(*) as cnt FROM return_requests GROUP BY status ORDER BY cnt DESC";
        String resolutionSql = "SELECT resolution_type, COUNT(*) as cnt FROM return_requests GROUP BY resolution_type";

        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(reasonSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    returnsByReason.put(rs.getString("reason"), rs.getInt("cnt"));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(statusSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    returnsByStatus.put(rs.getString("status"), rs.getInt("cnt"));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(resolutionSql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    returnsByResolution.put(rs.getString("resolution_type"), rs.getInt("cnt"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int totalReturns = returnDAO.count();
        BigDecimal totalRefundAmount = refundDAO.sumProcessed();
        int processedRefundsCount = refundDAO.countProcessed();

        req.setAttribute("returnsByReason", returnsByReason);
        req.setAttribute("returnsByStatus", returnsByStatus);
        req.setAttribute("returnsByResolution", returnsByResolution);
        req.setAttribute("totalReturns", totalReturns);
        req.setAttribute("totalRefundAmount", totalRefundAmount);
        req.setAttribute("processedRefundsCount", processedRefundsCount);

        req.getRequestDispatcher("/admin/reports.jsp").forward(req, resp);
    }
}
