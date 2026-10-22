package com.productreturn.servlet.customer;

import com.productreturn.dao.OrderDAO;
import com.productreturn.model.Order;
import com.productreturn.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

/**
 * "Thank you" page shown right after a successful checkout.
 */
public class OrderConfirmationServlet extends HttpServlet {

    /** Estimated business days from order to delivery (displayed as an estimate only). */
    private static final int ESTIMATED_DELIVERY_DAYS = 5;

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
            return;
        }
        Order order = orderDAO.findByIdWithItems(id);
        if (order == null || order.getUserId() != user.getId()) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Order+not+found");
            return;
        }
        req.setAttribute("order", order);
        req.setAttribute("estimatedDelivery",
                Date.valueOf(order.getOrderDate().toLocalDate().plusDays(ESTIMATED_DELIVERY_DAYS)));
        req.getRequestDispatcher("/customer/order-confirmation.jsp").forward(req, resp);
    }
}
