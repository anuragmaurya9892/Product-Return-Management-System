package com.productreturn.servlet.customer;

import com.productreturn.dao.UserDAO;
import com.productreturn.model.User;
import com.productreturn.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("user");

        User user = userDAO.findById(sessionUser.getId());
        req.setAttribute("userProfile", user);

        req.getRequestDispatcher("/customer/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("user");

        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        if (fullName == null || fullName.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Full name cannot be empty.");
            req.setAttribute("userProfile", sessionUser);
            req.getRequestDispatcher("/customer/profile.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.findById(sessionUser.getId());
        user.setFullName(ValidationUtil.sanitizeInput(fullName));
        user.setPhone(phone != null ? phone.trim() : "");
        user.setAddress(ValidationUtil.sanitizeInput(address));

        if (userDAO.update(user)) {
            // Update session user as well
            session.setAttribute("user", user);
            req.setAttribute("successMessage", "Profile updated successfully!");
        } else {
            req.setAttribute("errorMessage", "Failed to update profile. Please try again.");
        }

        req.setAttribute("userProfile", user);
        req.getRequestDispatcher("/customer/profile.jsp").forward(req, resp);
    }
}
