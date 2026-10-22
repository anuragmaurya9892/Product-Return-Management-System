package com.productreturn.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Central pricing rules for the storefront.
 * <p>
 * All prices are tax-inclusive. Shipping is free at or above
 * {@link #FREE_SHIPPING_THRESHOLD}; otherwise a flat {@link #FLAT_SHIPPING_FEE} applies.
 * Totals are always computed on the server from database prices — never from
 * values submitted by the browser.
 * </p>
 */
public final class PricingUtil {

    /** Order subtotal at or above which shipping is free (INR). */
    public static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("999.00");

    /** Flat shipping fee for orders below the threshold (INR). */
    public static final BigDecimal FLAT_SHIPPING_FEE = new BigDecimal("49.00");

    /** Maximum quantity of a single product per order line. */
    public static final int MAX_QTY_PER_ITEM = 10;

    private PricingUtil() {
    }

    /** Returns {@code unitPrice * quantity}, rounded to 2 decimals. */
    public static BigDecimal lineTotal(BigDecimal unitPrice, int quantity) {
        if (unitPrice == null || quantity <= 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    /** Returns the shipping fee for a given subtotal (0 for an empty cart). */
    public static BigDecimal shippingFor(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() <= 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return subtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0
                ? BigDecimal.ZERO.setScale(2)
                : FLAT_SHIPPING_FEE;
    }

    /** Returns how much more the customer must add to qualify for free shipping (never negative). */
    public static BigDecimal amountToFreeShipping(BigDecimal subtotal) {
        BigDecimal s = subtotal == null ? BigDecimal.ZERO : subtotal;
        BigDecimal diff = FREE_SHIPPING_THRESHOLD.subtract(s);
        return diff.signum() > 0 ? diff.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO.setScale(2);
    }

    /** Returns progress towards free shipping as an integer percentage 0–100. */
    public static int freeShippingProgress(BigDecimal subtotal) {
        if (subtotal == null || subtotal.signum() <= 0) {
            return 0;
        }
        BigDecimal pct = subtotal.multiply(BigDecimal.valueOf(100))
                .divide(FREE_SHIPPING_THRESHOLD, 0, RoundingMode.DOWN);
        return Math.min(100, pct.intValue());
    }

    /** Returns {@code subtotal + shipping}. */
    public static BigDecimal grandTotal(BigDecimal subtotal) {
        BigDecimal s = subtotal == null ? BigDecimal.ZERO : subtotal;
        return s.add(shippingFor(s)).setScale(2, RoundingMode.HALF_UP);
    }
}
