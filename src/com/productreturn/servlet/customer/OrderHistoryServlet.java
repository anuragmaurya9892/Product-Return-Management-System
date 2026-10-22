package com.productreturn.servlet.customer;

import com.productreturn.dao.OrderDAO;
import com.productreturn.model.Order;
import com.productreturn.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class OrderHistoryServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        List<Order> orders = orderDAO.findByUserId(user.getId());
        req.setAttribute("orders", orders);

        req.getRequestDispatcher("/customer/orders.jsp").forward(req, resp);
    }
}
