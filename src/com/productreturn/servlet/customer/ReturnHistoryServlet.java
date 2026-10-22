package com.productreturn.servlet.customer;

import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.ReturnRequest;
import com.productreturn.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class ReturnHistoryServlet extends HttpServlet {

    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        List<ReturnRequest> returns = returnDAO.findByUserId(user.getId());
        req.setAttribute("returns", returns);

        req.getRequestDispatcher("/customer/returns.jsp").forward(req, resp);
    }
}
