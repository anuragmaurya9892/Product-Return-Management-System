package com.productreturn.servlet.admin;

import com.productreturn.dao.RefundDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.Refund;
import com.productreturn.model.ReturnRequest;
import com.productreturn.model.User;
import com.productreturn.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminReturnDetailServlet extends HttpServlet {

    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();
    private final RefundDAO refundDAO = new RefundDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/returns");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            ReturnRequest returnReq = returnDAO.findById(id);
            if (returnReq == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/returns?error=Return+request+not+found");
                return;
            }

            Refund refund = null;
            if ("REFUND".equalsIgnoreCase(returnReq.getResolutionType())) {
                refund = refundDAO.findByReturnRequestId(returnReq.getId());
            }

            // Determine allowed next statuses
            List<String> nextStatuses = getAllowedNextStatuses(returnReq.getStatus(), returnReq.getResolutionType());

            req.setAttribute("returnRequest", returnReq);
            req.setAttribute("refund", refund);
            req.setAttribute("nextStatuses", nextStatuses);
            req.getRequestDispatcher("/admin/return-detail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/returns");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User admin = (User) session.getAttribute("user");

        String idParam = req.getParameter("id");
        String newStatus = req.getParameter("newStatus");
        String adminNote = req.getParameter("adminNote");

        if (idParam == null || newStatus == null || newStatus.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/returns?error=Invalid+request");
            return;
        }

        try {
            int returnId = Integer.parseInt(idParam.trim());
            ReturnRequest returnReq = returnDAO.findById(returnId);
            if (returnReq == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/returns?error=Return+not+found");
                return;
            }

            newStatus = newStatus.trim().toUpperCase();
            List<String> allowed = getAllowedNextStatuses(returnReq.getStatus(), returnReq.getResolutionType());

            if (!allowed.contains(newStatus)) {
                resp.sendRedirect(req.getContextPath() + "/admin/return?id=" + returnId +
                        "&error=Invalid+status+transition+from+" + returnReq.getStatus() + "+to+" + newStatus);
                return;
            }

            String note = ValidationUtil.sanitizeInput(adminNote);
            String changedBy = admin.getFullName() + " (" + admin.getUsername() + ")";

            boolean success = returnDAO.updateStatus(returnId, newStatus, note, changedBy);
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/admin/return?id=" + returnId +
                        "&success=Status+updated+to+" + newStatus);
            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/return?id=" + returnId +
                        "&error=Failed+to+update+status+in+database");
            }

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/returns");
        }
    }

    public static List<String> getAllowedNextStatuses(String currentStatus, String resolutionType) {
        List<String> list = new ArrayList<>();
        if (currentStatus == null) return list;

        switch (currentStatus.toUpperCase()) {
            case "PENDING":
                list.add("APPROVED");
                list.add("REJECTED");
                break;
            case "APPROVED":
                list.add("ITEM_RECEIVED");
                break;
            case "ITEM_RECEIVED":
                if ("REFUND".equalsIgnoreCase(resolutionType)) {
                    list.add("REFUND_PROCESSED");
                } else if ("REPLACEMENT".equalsIgnoreCase(resolutionType)) {
                    list.add("REPLACEMENT_SHIPPED");
                }
                break;
            case "REJECTED":
            case "REFUND_PROCESSED":
            case "REPLACEMENT_SHIPPED":
                // Terminal states — no further transitions allowed
                break;
            default:
                break;
        }
        return list;
    }
}
