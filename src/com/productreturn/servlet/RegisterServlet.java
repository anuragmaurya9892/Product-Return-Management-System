package com.productreturn.servlet;

import com.productreturn.dao.UserDAO;
import com.productreturn.model.User;
import com.productreturn.util.SecurityUtil;
import com.productreturn.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        // Keep values for re-populating on error
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("fullName", fullName);
        req.setAttribute("phone", phone);
        req.setAttribute("address", address);

        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            req.setAttribute("errorMessage", "Username must be 3-50 characters with letters, numbers, or underscores only.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("errorMessage", "Please enter a valid email address.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Full name is required.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("errorMessage", "Password must be at least 8 characters long and include at least one letter and one number.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("errorMessage", "Passwords do not match.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // Check uniqueness
        if (userDAO.findByUsername(username.trim()) != null) {
            req.setAttribute("errorMessage", "This username is already taken. Please choose another.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        if (userDAO.findByEmail(email.trim()) != null) {
            req.setAttribute("errorMessage", "An account with this email already exists. Please log in.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // Create user - Enforce role CUSTOMER (ignoring any user-supplied role for security)
        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setEmail(email.trim().toLowerCase());
        newUser.setPasswordHash(SecurityUtil.hashPassword(password));
        newUser.setFullName(ValidationUtil.sanitizeInput(fullName));
        newUser.setPhone(phone != null ? phone.trim() : "");
        newUser.setAddress(ValidationUtil.sanitizeInput(address));
        newUser.setRole("CUSTOMER");

        int userId = userDAO.create(newUser);
        if (userId > 0) {
            resp.sendRedirect(req.getContextPath() + "/login?success=Account+created+successfully!+Please+log+in.");
        } else {
            req.setAttribute("errorMessage", "Registration failed due to a system error. Please try again.");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}
