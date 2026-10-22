package com.productreturn.util;

import java.util.Collections;
import java.util.List;

/**
 * Order fulfilment state machine.
 * <pre>
 *   PLACED  --&gt; SHIPPED --&gt; DELIVERED (terminal)
 *   PLACED  --&gt; CANCELLED (terminal)
 *   SHIPPED --&gt; CANCELLED (admin only, e.g. lost in transit)
 * </pre>
 * Customers may only cancel orders that have not shipped yet.
 */
public final class OrderStatusRules {

    private OrderStatusRules() {
    }

    /** Next statuses an administrator may move an order to. */
    public static List<String> adminNextStatuses(String current) {
        if (current == null) {
            return Collections.emptyList();
        }
        switch (current.toUpperCase()) {
            case "PLACED":
                return List.of("SHIPPED", "CANCELLED");
            case "SHIPPED":
                return List.of("DELIVERED", "CANCELLED");
            default:
                return Collections.emptyList();
        }
    }

    /** Whether an administrator may move an order from {@code current} to {@code next}. */
    public static boolean canAdminTransition(String current, String next) {
        return next != null && adminNextStatuses(current).contains(next.toUpperCase());
    }

    /** Whether a customer may cancel an order in the given status. */
    public static boolean canCustomerCancel(String current) {
        return "PLACED".equalsIgnoreCase(current);
    }
}
