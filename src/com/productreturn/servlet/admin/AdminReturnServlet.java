package com.productreturn.servlet.admin;

import com.productreturn.dao.ReturnRequestDAO;
import com.productreturn.model.ReturnRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AdminReturnServlet extends HttpServlet {

    private final ReturnRequestDAO returnDAO = new ReturnRequestDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String status = req.getParameter("status");
        String resolution = req.getParameter("resolution");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");

        List<ReturnRequest> returns = returnDAO.searchAndFilter(search, status, resolution, dateFrom, dateTo);

        req.setAttribute("returns", returns);
        req.setAttribute("search", search != null ? search : "");
        req.setAttribute("status", status != null ? status : "ALL");
        req.setAttribute("resolution", resolution != null ? resolution : "ALL");
        req.setAttribute("dateFrom", dateFrom != null ? dateFrom : "");
        req.setAttribute("dateTo", dateTo != null ? dateTo : "");

        req.getRequestDispatcher("/admin/returns.jsp").forward(req, resp);
    }
}
