package com.productreturn;

import com.productreturn.util.DBConnection;
import com.productreturn.dao.UserDAO;
import com.productreturn.dao.ProductDAO;
import com.productreturn.dao.OrderDAO;
import com.productreturn.dao.ReturnRequestDAO;
import java.sql.Connection;

public class Phase3DbVerification {
    public static void main(String[] args) {
        System.out.println("--- Starting Phase 3: Database & Connection Layer Verification ---");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ JDBC Connection Successful! Connected to: " + conn.getCatalog());
            }

            UserDAO userDAO = new UserDAO();
            int customerCount = userDAO.countByRole("CUSTOMER");
            System.out.println("✅ Users Table: Found " + customerCount + " customers.");

            ProductDAO productDAO = new ProductDAO();
            int productCount = productDAO.count();
            System.out.println("✅ Products Table: Found " + productCount + " active products.");

            OrderDAO orderDAO = new OrderDAO();
            int orderCount = orderDAO.count();
            System.out.println("✅ Orders Table: Found " + orderCount + " orders.");

            ReturnRequestDAO returnDAO = new ReturnRequestDAO();
            int returnCount = returnDAO.count();
            System.out.println("✅ Return Requests Table: Found " + returnCount + " return requests.");

            System.out.println("--- Phase 3 Verification Complete: All tables and queries operational! ---");
        } catch (Exception e) {
            System.err.println("❌ Database Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
