package com.productreturn.servlet.customer;

import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.OrderItemDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.Order;
import com.productreturn.model.OrderItem;
import com.productreturn.model.ReturnRequest;
import com.productreturn.model.User;
import com.productreturn.util.OrderStatusRules;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailServlet extends HttpServlet {

    public static final int ELIGIBILITY_DAYS = 7;

    private final OrderDAO orderDAO = new OrderDAO();
    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam.trim());
            Order order = orderDAO.findByIdWithItems(orderId);

            // Enforce customer data isolation
            if (order == null || order.getUserId() != user.getId()) {
                resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Order+not+found");
                return;
            }

            // Calculate eligibility window
            boolean isDelivered = "DELIVERED".equalsIgnoreCase(order.getStatus()) && order.getDeliveredDate() != null;
            boolean withinReturnWindow = false;
            long daysSinceDelivery = -1;

            if (isDelivered) {
                LocalDate deliveryDate = order.getDeliveredDate().toLocalDate();
                LocalDate today = LocalDate.now();
                daysSinceDelivery = ChronoUnit.DAYS.between(deliveryDate, today);
                withinReturnWindow = daysSinceDelivery >= 0 && daysSinceDelivery <= ELIGIBILITY_DAYS;
            }

            // Map item return details
            Map<Integer, Boolean> eligibleMap = new HashMap<>();
            Map<Integer, ReturnRequest> activeReturnMap = new HashMap<>();
            Map<Integer, Integer> remainingQtyMap = new HashMap<>();

            for (OrderItem item : order.getItems()) {
                int returnedQty = orderItemDAO.getReturnedQuantity(item.getId());
                int remainingQty = item.getQuantity() - returnedQty;
                remainingQtyMap.put(item.getId(), remainingQty);

                List<ReturnRequest> returns = returnDAO.findByOrderItemId(item.getId());
                ReturnRequest activeReturn = null;
                for (ReturnRequest r : returns) {
                    if (!"REJECTED".equalsIgnoreCase(r.getStatus())) {
                        activeReturn = r;
                        break;
                    }
                }
                activeReturnMap.put(item.getId(), activeReturn);

                boolean canReturn = isDelivered && withinReturnWindow && (activeReturn == null) && (remainingQty > 0);
                eligibleMap.put(item.getId(), canReturn);
            }

            req.setAttribute("order", order);
            req.setAttribute("isDelivered", isDelivered);
            req.setAttribute("withinReturnWindow", withinReturnWindow);
            req.setAttribute("daysSinceDelivery", daysSinceDelivery);
            req.setAttribute("eligibilityDays", ELIGIBILITY_DAYS);
            req.setAttribute("eligibleMap", eligibleMap);
            req.setAttribute("activeReturnMap", activeReturnMap);
            req.setAttribute("remainingQtyMap", remainingQtyMap);
            req.setAttribute("canCancel", OrderStatusRules.canCustomerCancel(order.getStatus()));

            req.getRequestDispatcher("/customer/order-detail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String action = req.getParameter("action");
        String idParam = req.getParameter("orderId");

        if (!"cancel".equalsIgnoreCase(action) || idParam == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam.trim());
            boolean cancelled = orderDAO.cancelByCustomer(orderId, user.getId());
            if (cancelled) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + orderId + "&success=Order+cancelled+and+stock+restored");
            } else {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + orderId + "&error=Order+can+only+be+cancelled+before+it+is+shipped");
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
        }
    }
}
