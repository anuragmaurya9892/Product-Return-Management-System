package com.productreturn.servlet.admin;

import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.dao.UserDAO;
import com.productreturn.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminCustomerServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> customers = userDAO.findAllCustomers();

        Map<Integer, Integer> orderCountMap = new HashMap<>();
        Map<Integer, Integer> returnCountMap = new HashMap<>();

        for (User c : customers) {
            orderCountMap.put(c.getId(), orderDAO.findByUserId(c.getId()).size());
            returnCountMap.put(c.getId(), returnDAO.countByUserId(c.getId()));
        }

        req.setAttribute("customers", customers);
        req.setAttribute("orderCountMap", orderCountMap);
        req.setAttribute("returnCountMap", returnCountMap);

        req.getRequestDispatcher("/admin/customers.jsp").forward(req, resp);
    }
}
