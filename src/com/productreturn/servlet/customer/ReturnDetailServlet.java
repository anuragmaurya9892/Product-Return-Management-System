package com.productreturn.servlet.customer;

import com.productreturn.dao.RefundDAO;
import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.Refund;
import com.productreturn.model.ReturnRequest;
import com.productreturn.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ReturnDetailServlet extends HttpServlet {

    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();
    private final RefundDAO refundDAO = new RefundDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/customer/returns");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            ReturnRequest returnReq = returnDAO.findById(id);

            // Customer isolation check
            if (returnReq == null || returnReq.getUserId() != user.getId()) {
                resp.sendRedirect(req.getContextPath() + "/customer/returns?error=Return+request+not+found");
                return;
            }

            Refund refund = null;
            if ("REFUND".equalsIgnoreCase(returnReq.getResolutionType())) {
                refund = refundDAO.findByReturnRequestId(returnReq.getId());
            }

            req.setAttribute("returnRequest", returnReq);
            req.setAttribute("refund", refund);
            req.getRequestDispatcher("/customer/return-detail.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/customer/returns");
        }
    }
}
