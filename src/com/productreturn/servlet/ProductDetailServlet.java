package com.productreturn.servlet;

import com.productreturn.dao.ProductDAO;
import com.productreturn.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ProductDetailServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            Product product = productDAO.findById(id);
            if (product == null || !product.isActive()) {
                resp.sendRedirect(req.getContextPath() + "/products?error=Product+not+found");
                return;
            }
            req.setAttribute("product", product);
            req.getRequestDispatcher("/product-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
