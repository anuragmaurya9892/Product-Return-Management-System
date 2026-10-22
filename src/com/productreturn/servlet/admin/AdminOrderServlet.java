package com.productreturn.servlet.admin;

import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.OrderItemDAO;
import com.productreturn.model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AdminOrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                Order order = orderDAO.findByIdWithItems(id);
                req.setAttribute("order", order);
                req.getRequestDispatcher("/admin/order-detail.jsp").forward(req, resp);
                return;
            } catch (NumberFormatException ignored) {}
        }

        List<Order> orders = orderDAO.findAll();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/admin/orders.jsp").forward(req, resp);
    }
}
