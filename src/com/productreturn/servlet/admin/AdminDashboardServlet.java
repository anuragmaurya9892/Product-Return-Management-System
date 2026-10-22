package com.productreturn.servlet.admin;

import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.RefundDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.dao.UserDAO;
import com.productreturn.model.ReturnRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();
    private final RefundDAO refundDAO = new RefundDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int totalOrders = orderDAO.count();
        int pendingReturns = returnDAO.countByStatus("PENDING");
        int approvedReturns = returnDAO.countByStatus("APPROVED");
        int rejectedReturns = returnDAO.countByStatus("REJECTED");
        int totalCustomers = userDAO.countByRole("CUSTOMER");
        int processedRefundsCount = refundDAO.countProcessed();
        BigDecimal totalRefundAmount = refundDAO.sumProcessed();

        // Pending returns needing immediate attention
        List<ReturnRequest> pendingList = returnDAO.searchAndFilter(null, "PENDING", null, null, null);
        if (pendingList.size() > 5) {
            pendingList = pendingList.subList(0, 5);
        }

        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("pendingReturns", pendingReturns);
        req.setAttribute("approvedReturns", approvedReturns);
        req.setAttribute("rejectedReturns", rejectedReturns);
        req.setAttribute("totalCustomers", totalCustomers);
        req.setAttribute("processedRefundsCount", processedRefundsCount);
        req.setAttribute("totalRefundAmount", totalRefundAmount);
        req.setAttribute("pendingList", pendingList);

        req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
    }
}
