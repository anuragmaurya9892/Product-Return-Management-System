package com.productreturn;

import com.productreturn.servlet.admin.AdminReturnDetailServlet;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ReturnStatusWorkflowTest {

    @Test
    public void testPendingTransitions() {
        List<String> nextRefund = AdminReturnDetailServlet.getAllowedNextStatuses("PENDING", "REFUND");
        assertEquals(2, nextRefund.size());
        assertTrue(nextRefund.contains("APPROVED"));
        assertTrue(nextRefund.contains("REJECTED"));

        List<String> nextReplacement = AdminReturnDetailServlet.getAllowedNextStatuses("PENDING", "REPLACEMENT");
        assertEquals(2, nextReplacement.size());
        assertTrue(nextReplacement.contains("APPROVED"));
        assertTrue(nextReplacement.contains("REJECTED"));
    }

    @Test
    public void testApprovedTransitions() {
        List<String> nextRefund = AdminReturnDetailServlet.getAllowedNextStatuses("APPROVED", "REFUND");
        assertEquals(1, nextRefund.size());
        assertTrue(nextRefund.contains("ITEM_RECEIVED"));

        List<String> nextReplacement = AdminReturnDetailServlet.getAllowedNextStatuses("APPROVED", "REPLACEMENT");
        assertEquals(1, nextReplacement.size());
        assertTrue(nextReplacement.contains("ITEM_RECEIVED"));
    }

    @Test
    public void testItemReceivedTransitions() {
        // Refund workflow
        List<String> nextRefund = AdminReturnDetailServlet.getAllowedNextStatuses("ITEM_RECEIVED", "REFUND");
        assertEquals(1, nextRefund.size());
        assertTrue(nextRefund.contains("REFUND_PROCESSED"));
        assertFalse(nextRefund.contains("REPLACEMENT_SHIPPED"));

        // Replacement workflow
        List<String> nextReplacement = AdminReturnDetailServlet.getAllowedNextStatuses("ITEM_RECEIVED", "REPLACEMENT");
        assertEquals(1, nextReplacement.size());
        assertTrue(nextReplacement.contains("REPLACEMENT_SHIPPED"));
        assertFalse(nextReplacement.contains("REFUND_PROCESSED"));
    }

    @Test
    public void testTerminalStatesCannotTransition() {
        // Rejected cannot transition
        List<String> nextRejected = AdminReturnDetailServlet.getAllowedNextStatuses("REJECTED", "REFUND");
        assertTrue("Rejected state must be terminal", nextRejected.isEmpty());

        // Refund processed cannot transition
        List<String> nextRefunded = AdminReturnDetailServlet.getAllowedNextStatuses("REFUND_PROCESSED", "REFUND");
        assertTrue("Refund Processed state must be terminal", nextRefunded.isEmpty());

        // Replacement shipped cannot transition
        List<String> nextShipped = AdminReturnDetailServlet.getAllowedNextStatuses("REPLACEMENT_SHIPPED", "REPLACEMENT");
        assertTrue("Replacement Shipped state must be terminal", nextShipped.isEmpty());
    }
}
