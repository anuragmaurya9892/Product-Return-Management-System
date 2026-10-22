package com.productreturn.dao;

import com.productreturn.model.Order;
import com.productreturn.model.OrderItem;
import com.productreturn.model.PlaceOrderResult;
import com.productreturn.model.User;
import com.productreturn.util.DBConnection;
import com.productreturn.util.OrderStatusRules;
import com.productreturn.util.PricingUtil;
import com.productreturn.util.SecurityUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public Order findById(int id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order findByIdWithItems(int id) {
        Order order = findById(id);
        if (order != null) {
            OrderItemDAO itemDAO = new OrderItemDAO();
            List<OrderItem> items = itemDAO.findByOrderId(order.getId());
            order.setItems(items);

            UserDAO userDAO = new UserDAO();
            order.setUser(userDAO.findById(order.getUserId()));
        }
        return order;
    }

    public Order findByOrderNumber(String orderNumber) {
        String sql = "SELECT * FROM orders WHERE order_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> findByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC, id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.username, u.full_name, u.email FROM orders o " +
                     "JOIN users u ON o.user_id = u.id ORDER BY o.order_date DESC, o.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapRow(rs);
                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setFullName(rs.getString("full_name"));
                u.setEmail(rs.getString("email"));
                o.setUser(u);
                list.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int create(Order order) {
        String sql = "INSERT INTO orders (user_id, order_number, total_amount, status, order_date, delivered_date, shipping_address) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, order.getUserId());
            ps.setString(2, order.getOrderNumber());
            ps.setBigDecimal(3, order.getTotalAmount());
            ps.setString(4, order.getStatus());
            ps.setDate(5, order.getOrderDate());
            ps.setDate(6, order.getDeliveredDate());
            ps.setString(7, order.getShippingAddress());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        order.setId(id);
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setOrderNumber(rs.getString("order_number"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setOrderDate(rs.getDate("order_date"));
        o.setDeliveredDate(rs.getDate("delivered_date"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setShippingFee(rs.getBigDecimal("shipping_fee"));
        o.setShippedDate(rs.getDate("shipped_date"));
        o.setCancelledAt(rs.getTimestamp("cancelled_at"));
        return o;
    }

    // =====================================================================
    // Checkout & fulfilment (all multi-step operations run in a transaction)
    // =====================================================================

    /**
     * Converts the user's cart into an order atomically:
     * locks cart + product rows, re-validates price/stock from the database,
     * inserts the order and its items, decrements stock and empties the cart.
     */
    public PlaceOrderResult placeOrder(int userId, String shippingAddress, String paymentMethod) {
        String cartSql = "SELECT ci.product_id, ci.quantity, p.name, p.price, p.stock_quantity, p.active " +
                         "FROM cart_items ci JOIN products p ON ci.product_id = p.id " +
                         "WHERE ci.user_id = ? ORDER BY ci.product_id FOR UPDATE";
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            List<int[]> lines = new ArrayList<>();          // [productId, qty]
            List<BigDecimal> prices = new ArrayList<>();
            BigDecimal subtotal = BigDecimal.ZERO;

            try (PreparedStatement ps = conn.prepareStatement(cartSql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("name");
                        int qty = rs.getInt("quantity");
                        if (!rs.getBoolean("active")) {
                            conn.rollback();
                            return PlaceOrderResult.fail("\"" + name + "\" is no longer available. Please remove it from your cart.");
                        }
                        if (rs.getInt("stock_quantity") < qty) {
                            conn.rollback();
                            return PlaceOrderResult.fail("Only " + rs.getInt("stock_quantity") + " unit(s) of \"" + name + "\" left in stock. Please update your cart.");
                        }
                        BigDecimal price = rs.getBigDecimal("price");
                        lines.add(new int[]{rs.getInt("product_id"), qty});
                        prices.add(price);
                        subtotal = subtotal.add(PricingUtil.lineTotal(price, qty));
                    }
                }
            }

            if (lines.isEmpty()) {
                conn.rollback();
                return PlaceOrderResult.fail("Your cart is empty.");
            }

            BigDecimal shipping = PricingUtil.shippingFor(subtotal);
            BigDecimal total = subtotal.add(shipping);
            String orderNumber = SecurityUtil.generateOrderNumber();
            String paymentStatus = "ONLINE_DEMO".equals(paymentMethod) ? "PAID" : "PENDING";

            int orderId;
            String insertOrder = "INSERT INTO orders (user_id, order_number, total_amount, status, order_date, " +
                                 "shipping_address, payment_method, payment_status, shipping_fee) " +
                                 "VALUES (?, ?, ?, 'PLACED', ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setString(2, orderNumber);
                ps.setBigDecimal(3, total);
                ps.setDate(4, Date.valueOf(LocalDate.now()));
                ps.setString(5, shippingAddress);
                ps.setString(6, paymentMethod);
                ps.setString(7, paymentStatus);
                ps.setBigDecimal(8, shipping);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return PlaceOrderResult.fail("Could not create the order. Please try again.");
                    }
                    orderId = rs.getInt(1);
                }
            }

            String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
            String decStock = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE id = ? AND stock_quantity >= ?";
            try (PreparedStatement pi = conn.prepareStatement(insertItem);
                 PreparedStatement pd = conn.prepareStatement(decStock)) {
                for (int i = 0; i < lines.size(); i++) {
                    int productId = lines.get(i)[0];
                    int qty = lines.get(i)[1];
                    BigDecimal price = prices.get(i);

                    pi.setInt(1, orderId);
                    pi.setInt(2, productId);
                    pi.setInt(3, qty);
                    pi.setBigDecimal(4, price);
                    pi.setBigDecimal(5, PricingUtil.lineTotal(price, qty));
                    pi.addBatch();

                    pd.setInt(1, qty);
                    pd.setInt(2, productId);
                    pd.setInt(3, qty);
                    if (pd.executeUpdate() != 1) {
                        conn.rollback();
                        return PlaceOrderResult.fail("An item in your cart just went out of stock. Please review your cart.");
                    }
                }
                pi.executeBatch();
            }

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cart_items WHERE user_id = ?")) {
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

            conn.commit();
            return PlaceOrderResult.ok(orderId, orderNumber);
        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return PlaceOrderResult.fail("We couldn't place your order due to a system error. You have not been charged.");
        } finally {
            closeQuietly(conn);
        }
    }

    /** Customer-initiated cancellation (only for PLACED orders owned by the user). */
    public boolean cancelByCustomer(int orderId, int userId) {
        return transition(orderId, userId, "CANCELLED", true);
    }

    /** Admin fulfilment update, validated against {@link OrderStatusRules}. */
    public boolean updateStatusByAdmin(int orderId, String newStatus) {
        return transition(orderId, -1, newStatus, false);
    }

    private boolean transition(int orderId, int ownerUserId, String newStatus, boolean customer) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String current;
            String paymentMethod;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_id, status, payment_method FROM orders WHERE id = ? FOR UPDATE")) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { conn.rollback(); return false; }
                    if (customer && rs.getInt("user_id") != ownerUserId) { conn.rollback(); return false; }
                    current = rs.getString("status");
                    paymentMethod = rs.getString("payment_method");
                }
            }

            boolean allowed = customer
                    ? ("CANCELLED".equals(newStatus) && OrderStatusRules.canCustomerCancel(current))
                    : OrderStatusRules.canAdminTransition(current, newStatus);
            if (!allowed) { conn.rollback(); return false; }

            Date today = Date.valueOf(LocalDate.now());
            switch (newStatus) {
                case "SHIPPED":
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE orders SET status = 'SHIPPED', shipped_date = ? WHERE id = ?")) {
                        ps.setDate(1, today);
                        ps.setInt(2, orderId);
                        ps.executeUpdate();
                    }
                    break;
                case "DELIVERED":
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE orders SET status = 'DELIVERED', delivered_date = ?, " +
                            "payment_status = CASE WHEN payment_method = 'COD' THEN 'PAID' ELSE payment_status END " +
                            "WHERE id = ?")) {
                        ps.setDate(1, today);
                        ps.setInt(2, orderId);
                        ps.executeUpdate();
                    }
                    break;
                case "CANCELLED":
                    // Restock every item on the order
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE products p JOIN order_items oi ON oi.product_id = p.id " +
                            "SET p.stock_quantity = p.stock_quantity + oi.quantity WHERE oi.order_id = ?")) {
                        ps.setInt(1, orderId);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                            "UPDATE orders SET status = 'CANCELLED', payment_status = 'VOID', " +
                            "cancelled_at = CURRENT_TIMESTAMP WHERE id = ?")) {
                        ps.setInt(1, orderId);
                        ps.executeUpdate();
                    }
                    break;
                default:
                    conn.rollback();
                    return false;
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            rollbackQuietly(conn);
            e.printStackTrace();
            return false;
        } finally {
            closeQuietly(conn);
        }
    }

    /** All orders, optionally filtered by status (null/ALL = no filter). */
    public List<Order> findAll(String status) {
        if (status == null || status.isBlank() || "ALL".equalsIgnoreCase(status)) {
            return findAll();
        }
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.username, u.full_name, u.email FROM orders o " +
                     "JOIN users u ON o.user_id = u.id WHERE o.status = ? ORDER BY o.order_date DESC, o.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapRow(rs);
                    User u = new User();
                    u.setId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setFullName(rs.getString("full_name"));
                    u.setEmail(rs.getString("email"));
                    o.setUser(u);
                    list.add(o);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try { conn.rollback(); } catch (SQLException ignored) { }
        }
    }

    private static void closeQuietly(Connection conn) {
        if (conn != null) {
            try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignored) { }
        }
    }
}
