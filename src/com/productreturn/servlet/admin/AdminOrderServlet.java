package com.productreturn.servlet.admin;

import com.productreturn.dao.OrderDAO;
import com.productreturn.model.Order;
import com.productreturn.util.OrderStatusRules;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AdminOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                Order order = orderDAO.findByIdWithItems(id);
                if (order == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/orders?error=Order+not+found");
                    return;
                }
                req.setAttribute("order", order);
                req.setAttribute("nextStatuses", OrderStatusRules.adminNextStatuses(order.getStatus()));
                req.getRequestDispatcher("/admin/order-detail.jsp").forward(req, resp);
                return;
            } catch (NumberFormatException ignored) {}
        }

        String statusFilter = req.getParameter("status");
        List<Order> orders = orderDAO.findAll(statusFilter);
        req.setAttribute("orders", orders);
        req.setAttribute("selectedStatus", statusFilter);
        req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("orderId");
        String newStatus = req.getParameter("newStatus");
        String fromList = req.getParameter("fromList");

        if (idParam == null || newStatus == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders?error=Invalid+request");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam.trim());
            boolean updated = orderDAO.updateStatusByAdmin(orderId, newStatus.trim().toUpperCase());
            String redirectBase = "1".equals(fromList)
                    ? req.getContextPath() + "/admin/orders"
                    : req.getContextPath() + "/admin/orders?id=" + orderId;
            String sep = redirectBase.contains("?") ? "&" : "?";

            if (updated) {
                resp.sendRedirect(redirectBase + sep + "success=Order+status+updated+to+" + newStatus.trim().toUpperCase());
            } else {
                resp.sendRedirect(redirectBase + sep + "error=Invalid+status+transition");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders?error=Invalid+order+ID");
        }
    }
}
