package com.productreturn.servlet;

import com.productreturn.dao.ProductDAO;
import com.productreturn.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ProductCatalogueServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String search = req.getParameter("search");
        String category = req.getParameter("category");

        List<Product> products = productDAO.searchWithFilters(search, category);
        List<String> categories = productDAO.getCategories();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("selectedCategory", category != null ? category : "ALL");
        req.setAttribute("searchQuery", search != null ? search : "");

        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }
}
