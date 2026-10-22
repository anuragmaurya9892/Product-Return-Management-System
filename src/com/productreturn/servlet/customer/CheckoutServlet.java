package com.productreturn.servlet.customer;

import com.productreturn.dao.CartDAO;
import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.UserDAO;
import com.productreturn.model.CartItem;
import com.productreturn.model.PlaceOrderResult;
import com.productreturn.model.User;
import com.productreturn.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Checkout: review cart, confirm delivery address and payment method, place order.
 */
public class CheckoutServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<CartItem> items = cartDAO.findByUserId(user.getId());
        if (items.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/cart?error=Your+cart+is+empty.");
            return;
        }
        User fresh = userDAO.findById(user.getId());
        req.setAttribute("fullName", fresh.getFullName());
        req.setAttribute("phone", fresh.getPhone());
        req.setAttribute("address", fresh.getAddress());
        req.setAttribute("paymentMethod", "COD");
        render(req, resp, items);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String fullName = trim(req.getParameter("fullName"));
        String phone = trim(req.getParameter("phone"));
        String address = trim(req.getParameter("address"));
        String paymentMethod = trim(req.getParameter("paymentMethod"));

        req.setAttribute("fullName", fullName);
        req.setAttribute("phone", phone);
        req.setAttribute("address", address);
        req.setAttribute("paymentMethod", paymentMethod);

        String error = null;
        if (fullName.isEmpty() || fullName.length() > 100) {
            error = "Please enter the recipient's full name.";
        } else if (!ValidationUtil.isValidPhone(phone)) {
            error = "Please enter a valid phone number for delivery updates.";
        } else if (address.length() < 10 || address.length() > 500) {
            error = "Please enter a complete delivery address (10–500 characters).";
        } else if (!"COD".equals(paymentMethod) && !"ONLINE_DEMO".equals(paymentMethod)) {
            error = "Please choose a payment method.";
        }

        if (error != null) {
            req.setAttribute("errorMessage", error);
            render(req, resp, cartDAO.findByUserId(user.getId()));
            return;
        }

        String shippingAddress = ValidationUtil.sanitizeInput(fullName) + "\n" +
                                 ValidationUtil.sanitizeInput(address) + "\nPhone: " + phone;

        PlaceOrderResult result = orderDAO.placeOrder(user.getId(), shippingAddress, paymentMethod);
        if (!result.isSuccess()) {
            req.setAttribute("errorMessage", result.getErrorMessage());
            render(req, resp, cartDAO.findByUserId(user.getId()));
            return;
        }

        session.setAttribute("cartCount", 0);
        resp.sendRedirect(req.getContextPath() + "/customer/order/confirmation?id=" + result.getOrderId());
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, List<CartItem> items)
            throws ServletException, IOException {
        if (items.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/cart?error=Your+cart+is+empty.");
            return;
        }
        CartServlet.populateTotals(req, items);
        req.getRequestDispatcher("/customer/checkout.jsp").forward(req, resp);
    }

    private static String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
