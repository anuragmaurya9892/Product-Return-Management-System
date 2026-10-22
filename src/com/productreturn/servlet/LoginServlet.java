package com.productreturn.servlet;

import com.productreturn.dao.UserDAO;
import com.productreturn.model.User;
import com.productreturn.util.SecurityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("logout".equalsIgnoreCase(action)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login?success=You+have+been+logged+out+successfully");
            return;
        }

        // If already logged in, redirect to respective dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                resp.sendRedirect(req.getContextPath() + "/customer/dashboard");
            }
            return;
        }

        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String identifier = req.getParameter("identifier"); // username or email
        String password = req.getParameter("password");

        if (identifier == null || identifier.trim().isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("errorMessage", "Please provide both username/email and password.");
            req.setAttribute("identifier", identifier);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        identifier = identifier.trim();
        User user = userDAO.findByUsername(identifier);
        if (user == null) {
            user = userDAO.findByEmail(identifier);
        }

        if (user == null || !SecurityUtil.checkPassword(password, user.getPasswordHash())) {
            req.setAttribute("errorMessage", "Invalid credentials. Please try again.");
            req.setAttribute("identifier", identifier);
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // Authentication successful
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/customer/dashboard");
        }
    }
}
