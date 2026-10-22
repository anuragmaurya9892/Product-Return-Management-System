package com.productreturn;

import com.productreturn.util.OrderStatusRules;
import com.productreturn.util.PricingUtil;
import com.productreturn.util.SecurityUtil;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class SecurityUtilTest {

    @Test
    public void testPasswordHashingAndVerification() {
        String plain = "CustomerSecret2026";
        String hash1 = SecurityUtil.hashPassword(plain);
        String hash2 = SecurityUtil.hashPassword(plain);

        assertNotNull(hash1);
        assertNotNull(hash2);
        // Salt ensures two hashes of identical passwords are distinct
        assertNotEquals(hash1, hash2);

        assertTrue("Password should match hash1", SecurityUtil.checkPassword(plain, hash1));
        assertTrue("Password should match hash2", SecurityUtil.checkPassword(plain, hash2));
        assertFalse("Wrong password should be rejected", SecurityUtil.checkPassword("WrongPassword", hash1));
    }

    @Test
    public void testCSRFTokenGeneration() {
        String token1 = SecurityUtil.generateCSRFToken();
        String token2 = SecurityUtil.generateCSRFToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertTrue(token1.length() >= 32);
        assertNotEquals(token1, token2);
    }

    @Test
    public void testReferenceNumberFormats() {
        String returnNum = SecurityUtil.generateReturnNumber();
        String orderNum = SecurityUtil.generateOrderNumber();

        assertNotNull(returnNum);
        assertNotNull(orderNum);

        assertTrue("Return number must match RET-yyyyMMdd-NNNN format: " + returnNum,
                returnNum.matches("^RET-\\d{8}-\\d{4}$"));

        assertTrue("Order number must match ORD-yyyyMMdd-NNNNNN format: " + orderNum,
                orderNum.matches("^ORD-\\d{8}-\\d{4,6}$"));
    }

    @Test
    public void testPricingAndFreeShippingRules() {
        BigDecimal below = new BigDecimal("500.00");
        assertEquals(new BigDecimal("49.00"), PricingUtil.shippingFor(below));
        assertEquals(new BigDecimal("499.00"), PricingUtil.amountToFreeShipping(below));
        assertEquals(new BigDecimal("549.00"), PricingUtil.grandTotal(below));

        BigDecimal above = new BigDecimal("1200.00");
        assertEquals(new BigDecimal("0.00"), PricingUtil.shippingFor(above));
        assertEquals(new BigDecimal("0.00"), PricingUtil.amountToFreeShipping(above));
        assertEquals(100, PricingUtil.freeShippingProgress(above));
        assertEquals(new BigDecimal("1200.00"), PricingUtil.grandTotal(above));
    }

    @Test
    public void testOrderFulfilmentWorkflowRules() {
        assertTrue(OrderStatusRules.canCustomerCancel("PLACED"));
        assertFalse(OrderStatusRules.canCustomerCancel("SHIPPED"));
        assertFalse(OrderStatusRules.canCustomerCancel("DELIVERED"));

        assertTrue(OrderStatusRules.canAdminTransition("PLACED", "SHIPPED"));
        assertTrue(OrderStatusRules.canAdminTransition("PLACED", "CANCELLED"));
        assertTrue(OrderStatusRules.canAdminTransition("SHIPPED", "DELIVERED"));
        assertFalse(OrderStatusRules.canAdminTransition("DELIVERED", "CANCELLED"));
    }

    @Test
    public void testHtmlEscaping() {
        assertEquals("", SecurityUtil.escapeHtml(null));
        assertEquals("&lt;div&gt;Hello &amp; Welcome&lt;/div&gt;",
                SecurityUtil.escapeHtml("<div>Hello & Welcome</div>"));
        assertEquals("&#x27;quoted&#x27; &quot;value&quot;",
                SecurityUtil.escapeHtml("'quoted' \"value\""));
    }
}
