package com.productreturn.servlet.customer;

import com.productreturn.dao.CartDAO;
import com.productreturn.dao.ProductDAO;
import com.productreturn.model.CartItem;
import com.productreturn.model.Product;
import com.productreturn.model.User;
import com.productreturn.util.PricingUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Shopping cart controller.
 * <ul>
 *   <li>GET  /customer/cart — view cart</li>
 *   <li>POST /customer/cart — action = add | update | remove | clear
 *       (add ?ajax=1 to receive JSON instead of a redirect)</li>
 * </ul>
 */
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<CartItem> items = cartDAO.findByUserId(user.getId());
        populateTotals(req, items);
        refreshCount(req.getSession(), user.getId());
        req.getRequestDispatcher("/customer/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        boolean ajax = "1".equals(req.getParameter("ajax"));
        String action = req.getParameter("action");
        int productId = parseInt(req.getParameter("productId"), -1);
        int qty = parseInt(req.getParameter("quantity"), 1);

        boolean ok = true;
        String message;

        if ("clear".equals(action)) {
            cartDAO.clear(user.getId());
            message = "Your cart has been cleared.";
        } else {
            Product p = productId > 0 ? productDAO.findById(productId) : null;
            if (p == null || !p.isActive()) {
                ok = false;
                message = "That product is not available.";
            } else if ("remove".equals(action)) {
                cartDAO.remove(user.getId(), productId);
                message = p.getName() + " removed from your cart.";
            } else if ("update".equals(action)) {
                int cap = Math.min(p.getStockQuantity(), PricingUtil.MAX_QTY_PER_ITEM);
                int newQty = Math.max(0, Math.min(qty, cap));
                cartDAO.setQuantity(user.getId(), productId, newQty);
                message = newQty == 0 ? p.getName() + " removed from your cart."
                        : (qty > cap ? "Only " + cap + " unit(s) of " + p.getName() + " can be ordered." : "Cart updated.");
            } else { // add (default)
                if (p.getStockQuantity() <= 0) {
                    ok = false;
                    message = p.getName() + " is out of stock.";
                } else {
                    int cap = Math.min(p.getStockQuantity(), PricingUtil.MAX_QTY_PER_ITEM);
                    int existing = cartDAO.quantityOf(user.getId(), productId);
                    int newQty = Math.min(existing + Math.max(1, qty), cap);
                    if (newQty == existing) {
                        ok = false;
                        message = "You already have the maximum quantity (" + cap + ") of " + p.getName() + " in your cart.";
                    } else {
                        cartDAO.setQuantity(user.getId(), productId, newQty);
                        message = p.getName() + " added to your cart.";
                    }
                }
            }
        }

        int count = refreshCount(session, user.getId());

        if (ajax) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write("{\"ok\":" + ok + ",\"cartCount\":" + count +
                    ",\"message\":\"" + jsonEscape(message) + "\"}");
            return;
        }

        if (ok && "1".equals(req.getParameter("buyNow"))) {
            resp.sendRedirect(req.getContextPath() + "/customer/checkout");
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/customer/cart?" + (ok ? "success=" : "error=") +
                URLEncoder.encode(message, StandardCharsets.UTF_8));
    }

    /** Puts subtotal, shipping, totals and free-shipping progress on the request. */
    static void populateTotals(HttpServletRequest req, List<CartItem> items) {
        BigDecimal subtotal = CartDAO.subtotal(items);
        boolean hasUnavailable = false;
        int units = 0;
        for (CartItem ci : items) {
            units += ci.getQuantity();
            if (!ci.isAvailable()) hasUnavailable = true;
        }
        req.setAttribute("cartItems", items);
        req.setAttribute("cartUnits", units);
        req.setAttribute("subtotal", subtotal);
        req.setAttribute("shippingFee", PricingUtil.shippingFor(subtotal));
        req.setAttribute("grandTotal", PricingUtil.grandTotal(subtotal));
        req.setAttribute("amountToFreeShipping", PricingUtil.amountToFreeShipping(subtotal));
        req.setAttribute("freeShippingProgress", PricingUtil.freeShippingProgress(subtotal));
        req.setAttribute("freeShippingThreshold", PricingUtil.FREE_SHIPPING_THRESHOLD);
        req.setAttribute("hasUnavailable", hasUnavailable);
    }

    static int refreshCount(HttpSession session, int userId) {
        int count = new CartDAO().countUnits(userId);
        session.setAttribute("cartCount", count);
        return count;
    }

    private static int parseInt(String s, int def) {
        try {
            return s == null ? def : Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static String jsonEscape(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '<': sb.append("\\u003c"); break;
                case '>': sb.append("\\u003e"); break;
                case '\n': sb.append("\\n"); break;
                default:
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c)); else sb.append(c);
            }
        }
        return sb.toString();
    }
}
