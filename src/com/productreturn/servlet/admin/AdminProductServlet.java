package com.productreturn.servlet.admin;

import com.productreturn.dao.ProductDAO;
import com.productreturn.model.Product;
import com.productreturn.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class AdminProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("edit".equalsIgnoreCase(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam.trim());
                    Product product = productDAO.findById(id);
                    req.setAttribute("product", product);
                } catch (NumberFormatException ignored) {}
            }
            req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
            return;
        }

        if ("new".equalsIgnoreCase(action)) {
            req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
            return;
        }

        if ("toggle".equalsIgnoreCase(action)) {
            String idParam = req.getParameter("id");
            if (idParam != null) {
                try {
                    int id = Integer.parseInt(idParam.trim());
                    Product product = productDAO.findById(id);
                    if (product != null) {
                        product.setActive(!product.isActive());
                        productDAO.update(product);
                    }
                } catch (NumberFormatException ignored) {}
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products?success=Product+status+updated");
            return;
        }

        List<Product> products = productDAO.findAll();
        req.setAttribute("products", products);
        req.getRequestDispatcher("/admin/products.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String name = req.getParameter("name");
        String sku = req.getParameter("sku");
        String category = req.getParameter("category");
        String description = req.getParameter("description");
        String priceParam = req.getParameter("price");
        String stockParam = req.getParameter("stockQuantity");
        String imageUrl = req.getParameter("imageUrl");
        boolean active = "on".equalsIgnoreCase(req.getParameter("active")) || "true".equalsIgnoreCase(req.getParameter("active"));

        if (name == null || name.trim().isEmpty() || sku == null || sku.trim().isEmpty() ||
                priceParam == null || priceParam.trim().isEmpty()) {
            req.setAttribute("errorMessage", "Name, SKU, and Price are required fields.");
            req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
            return;
        }

        try {
            BigDecimal price = new BigDecimal(priceParam.trim());
            int stock = stockParam != null && !stockParam.trim().isEmpty() ? Integer.parseInt(stockParam.trim()) : 0;

            if (idParam != null && !idParam.trim().isEmpty()) {
                // Update
                int id = Integer.parseInt(idParam.trim());
                Product p = productDAO.findById(id);
                if (p != null) {
                    p.setName(ValidationUtil.sanitizeInput(name));
                    p.setSku(sku.trim().toUpperCase());
                    p.setCategory(ValidationUtil.sanitizeInput(category));
                    p.setDescription(description != null ? description.trim() : "");
                    p.setPrice(price);
                    p.setStockQuantity(stock);
                    p.setImageUrl(imageUrl != null && !imageUrl.trim().isEmpty() ? imageUrl.trim() : "/images/placeholder.png");
                    p.setActive(active);
                    productDAO.update(p);
                    resp.sendRedirect(req.getContextPath() + "/admin/products?success=Product+updated+successfully");
                    return;
                }
            } else {
                // Create
                Product p = new Product();
                p.setName(ValidationUtil.sanitizeInput(name));
                p.setSku(sku.trim().toUpperCase());
                p.setCategory(ValidationUtil.sanitizeInput(category));
                p.setDescription(description != null ? description.trim() : "");
                p.setPrice(price);
                p.setStockQuantity(stock);
                p.setImageUrl(imageUrl != null && !imageUrl.trim().isEmpty() ? imageUrl.trim() : "/images/placeholder.png");
                p.setActive(active);
                productDAO.create(p);
                resp.sendRedirect(req.getContextPath() + "/admin/products?success=Product+created+successfully");
                return;
            }

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid numeric value for price or stock quantity.");
            req.getRequestDispatcher("/admin/product-form.jsp").forward(req, resp);
        }
    }
}
