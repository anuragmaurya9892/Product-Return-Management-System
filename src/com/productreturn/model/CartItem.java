package com.productreturn.model;

import com.productreturn.util.PricingUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * A product line in a customer's persistent shopping cart.
 */
public class CartItem {

    private int id;
    private int userId;
    private int productId;
    private int quantity;
    private Timestamp addedAt;

    /** Transient reference to the product (loaded with current price and stock). */
    private Product product;

    public CartItem() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Timestamp getAddedAt() { return addedAt; }
    public void setAddedAt(Timestamp addedAt) { this.addedAt = addedAt; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    /** Current line total using the live product price. */
    public BigDecimal getLineTotal() {
        return product == null ? BigDecimal.ZERO : PricingUtil.lineTotal(product.getPrice(), quantity);
    }

    /** True if the product can still be purchased in the requested quantity. */
    public boolean isAvailable() {
        return product != null && product.isActive() && product.getStockQuantity() >= quantity;
    }
}
