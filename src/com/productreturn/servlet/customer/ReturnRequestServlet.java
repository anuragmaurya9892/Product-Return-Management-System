package com.productreturn.servlet.customer;

import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.OrderItemDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.Order;
import com.productreturn.model.OrderItem;
import com.productreturn.model.ReturnRequest;
import com.productreturn.model.User;
import com.productreturn.util.SecurityUtil;
import com.productreturn.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,        // 1 MB
        maxFileSize = 5 * 1024 * 1024,           // 5 MB
        maxRequestSize = 10 * 1024 * 1024        // 10 MB
)
public class ReturnRequestServlet extends HttpServlet {

    public static final int ELIGIBILITY_DAYS = 7;
    private static final List<String> VALID_REASONS = Arrays.asList(
            "DAMAGED", "DEFECTIVE", "WRONG_ITEM", "INCORRECT_SIZE", "NOT_NEEDED"
    );
    private static final List<String> VALID_RESOLUTIONS = Arrays.asList(
            "REFUND", "REPLACEMENT"
    );

    private final OrderItemDAO orderItemDAO = new OrderItemDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String orderItemIdParam = req.getParameter("orderItemId");
        if (orderItemIdParam == null || orderItemIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
            return;
        }

        try {
            int orderItemId = Integer.parseInt(orderItemIdParam.trim());
            OrderItem item = orderItemDAO.findById(orderItemId);
            if (item == null) {
                resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Order+item+not+found");
                return;
            }

            Order order = orderDAO.findById(item.getOrderId());
            if (order == null || order.getUserId() != user.getId()) {
                resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Unauthorized+access");
                return;
            }

            // Check order delivery status
            if (!"DELIVERED".equalsIgnoreCase(order.getStatus()) || order.getDeliveredDate() == null) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=Returns+are+only+allowed+for+delivered+orders");
                return;
            }

            // Check 7-day eligibility window
            LocalDate deliveryDate = order.getDeliveredDate().toLocalDate();
            long days = ChronoUnit.DAYS.between(deliveryDate, LocalDate.now());
            if (days > ELIGIBILITY_DAYS) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=Return+window+of+" + ELIGIBILITY_DAYS + "+days+has+expired+for+this+order");
                return;
            }

            // Check for active duplicate returns
            if (returnDAO.hasActiveReturn(orderItemId)) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=An+active+return+request+already+exists+for+this+item");
                return;
            }

            // Check remaining quantity
            int returnedQty = orderItemDAO.getReturnedQuantity(orderItemId);
            int maxEligibleQty = item.getQuantity() - returnedQty;
            if (maxEligibleQty <= 0) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=All+items+from+this+order+line+have+already+been+returned");
                return;
            }

            req.setAttribute("item", item);
            req.setAttribute("order", order);
            req.setAttribute("maxEligibleQty", maxEligibleQty);
            req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        // Validate CSRF token
        String sessionToken = (String) session.getAttribute("csrfToken");
        String formToken = req.getParameter("csrfToken");
        if (sessionToken == null || !sessionToken.equals(formToken)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        String orderItemIdParam = req.getParameter("orderItemId");
        String quantityParam = req.getParameter("quantity");
        String reason = req.getParameter("reason");
        String resolutionType = req.getParameter("resolutionType");
        String description = req.getParameter("description");

        if (orderItemIdParam == null || quantityParam == null || reason == null || resolutionType == null) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Missing+required+fields");
            return;
        }

        try {
            int orderItemId = Integer.parseInt(orderItemIdParam.trim());
            int quantity = Integer.parseInt(quantityParam.trim());

            OrderItem item = orderItemDAO.findById(orderItemId);
            if (item == null) {
                resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Order+item+not+found");
                return;
            }

            Order order = orderDAO.findById(item.getOrderId());
            if (order == null || order.getUserId() != user.getId()) {
                resp.sendRedirect(req.getContextPath() + "/customer/orders?error=Unauthorized+access");
                return;
            }

            // Delivery check
            if (!"DELIVERED".equalsIgnoreCase(order.getStatus()) || order.getDeliveredDate() == null) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=Returns+are+only+allowed+for+delivered+orders");
                return;
            }

            // Eligibility window check
            LocalDate deliveryDate = order.getDeliveredDate().toLocalDate();
            long days = ChronoUnit.DAYS.between(deliveryDate, LocalDate.now());
            if (days > ELIGIBILITY_DAYS) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=Return+window+expired");
                return;
            }

            // Duplicate active return check
            if (returnDAO.hasActiveReturn(orderItemId)) {
                resp.sendRedirect(req.getContextPath() + "/customer/order?id=" + order.getId() +
                        "&error=An+active+return+request+already+exists");
                return;
            }

            // Quantity validation
            int returnedQty = orderItemDAO.getReturnedQuantity(orderItemId);
            int maxEligibleQty = item.getQuantity() - returnedQty;
            if (quantity <= 0 || quantity > maxEligibleQty) {
                req.setAttribute("errorMessage", "Invalid quantity. You can return between 1 and " + maxEligibleQty + " items.");
                req.setAttribute("item", item);
                req.setAttribute("order", order);
                req.setAttribute("maxEligibleQty", maxEligibleQty);
                req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);
                return;
            }

            // Reason and Resolution validation
            reason = reason.trim().toUpperCase();
            resolutionType = resolutionType.trim().toUpperCase();
            if (!VALID_REASONS.contains(reason)) {
                req.setAttribute("errorMessage", "Please select a valid return reason.");
                req.setAttribute("item", item);
                req.setAttribute("order", order);
                req.setAttribute("maxEligibleQty", maxEligibleQty);
                req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);
                return;
            }

            if (!VALID_RESOLUTIONS.contains(resolutionType)) {
                req.setAttribute("errorMessage", "Please select a valid resolution type (Refund or Replacement).");
                req.setAttribute("item", item);
                req.setAttribute("order", order);
                req.setAttribute("maxEligibleQty", maxEligibleQty);
                req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);
                return;
            }

            // File upload handling (optional evidence image)
            String savedImagePath = null;
            Part filePart = null;
            try {
                filePart = req.getPart("evidenceImage");
            } catch (Exception ignored) {
            }

            if (filePart != null && filePart.getSize() > 0) {
                if (!ValidationUtil.isValidFileSize(filePart.getSize())) {
                    req.setAttribute("errorMessage", "Image file size exceeds maximum limit of 5 MB.");
                    req.setAttribute("item", item);
                    req.setAttribute("order", order);
                    req.setAttribute("maxEligibleQty", maxEligibleQty);
                    req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);
                    return;
                }

                String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                if (!ValidationUtil.isValidImageFile(submittedFileName)) {
                    req.setAttribute("errorMessage", "Invalid file type. Only JPG, JPEG, and PNG images are allowed.");
                    req.setAttribute("item", item);
                    req.setAttribute("order", order);
                    req.setAttribute("maxEligibleQty", maxEligibleQty);
                    req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);
                    return;
                }

                // Save file safely
                String ext = submittedFileName.substring(submittedFileName.lastIndexOf('.'));
                String uniqueFileName = "evidence_" + UUID.randomUUID() + ext;

                // Save to application upload dir or uploads folder in project
                String uploadDir = req.getServletContext().getRealPath("/uploads");
                if (uploadDir == null) {
                    uploadDir = System.getProperty("user.home") + File.separator + "returnhub_uploads";
                }
                File dirFile = new File(uploadDir);
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }

                File destination = new File(dirFile, uniqueFileName);
                filePart.write(destination.getAbsolutePath());
                savedImagePath = "uploads/" + uniqueFileName;
            }

            // Create return request
            ReturnRequest returnReq = new ReturnRequest();
            returnReq.setOrderItemId(orderItemId);
            returnReq.setUserId(user.getId());
            returnReq.setReturnNumber(SecurityUtil.generateReturnNumber());
            returnReq.setQuantity(quantity);
            returnReq.setReason(reason);
            returnReq.setResolutionType(resolutionType);
            returnReq.setDescription(ValidationUtil.sanitizeInput(description));
            returnReq.setEvidenceImage(savedImagePath);
            returnReq.setStatus("PENDING");

            int returnId = returnDAO.create(returnReq);
            if (returnId > 0) {
                resp.sendRedirect(req.getContextPath() + "/customer/return?id=" + returnId +
                        "&success=Return+request+submitted+successfully!+Reference:+" + returnReq.getReturnNumber());
            } else {
                req.setAttribute("errorMessage", "Failed to submit return request due to database error.");
                req.setAttribute("item", item);
                req.setAttribute("order", order);
                req.setAttribute("maxEligibleQty", maxEligibleQty);
                req.getRequestDispatcher("/customer/return-form.jsp").forward(req, resp);
            }

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/customer/orders");
        }
    }
}
