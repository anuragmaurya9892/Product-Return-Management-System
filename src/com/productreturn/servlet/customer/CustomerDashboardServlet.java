package com.productreturn.servlet.customer;

import com.productreturn.dao.CartDAO;
import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.Order;
import com.productreturn.model.ReturnRequest;
import com.productreturn.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class CustomerDashboardServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();
    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();
    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        int userId = user.getId();
        session.setAttribute("cartCount", cartDAO.countUnits(userId));

        List<Order> orders = orderDAO.findByUserId(userId);
        List<ReturnRequest> returns = returnDAO.findByUserId(userId);

        int totalOrders = orders.size();
        int totalReturns = returns.size();
        int pendingReturns = returnDAO.countByUserIdAndStatus(userId, "PENDING");
        int approvedReturns = returnDAO.countByUserIdAndStatus(userId, "APPROVED");

        List<Order> recentOrders = orders.size() > 5 ? orders.subList(0, 5) : orders;
        List<ReturnRequest> recentReturns = returns.size() > 5 ? returns.subList(0, 5) : returns;

        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("totalReturns", totalReturns);
        req.setAttribute("pendingReturns", pendingReturns);
        req.setAttribute("approvedReturns", approvedReturns);
        req.setAttribute("recentOrders", recentOrders);
        req.setAttribute("recentReturns", recentReturns);

        req.getRequestDispatcher("/customer/dashboard.jsp").forward(req, resp);
    }
}
