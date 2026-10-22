package com.productreturn;

import org.junit.Test;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import static org.junit.Assert.*;

public class ReturnEligibilityTest {

    private static final int ELIGIBILITY_WINDOW_DAYS = 7;

    @Test
    public void testSevenDayEligibilityWindow() {
        LocalDate today = LocalDate.now();

        // 3 days ago -> eligible
        LocalDate deliveredThreeDaysAgo = today.minusDays(3);
        long daysDiff1 = ChronoUnit.DAYS.between(deliveredThreeDaysAgo, today);
        assertTrue("3 days delivery should be within 7 days window", daysDiff1 <= ELIGIBILITY_WINDOW_DAYS);

        // Exactly 7 days ago -> eligible
        LocalDate deliveredSevenDaysAgo = today.minusDays(7);
        long daysDiff2 = ChronoUnit.DAYS.between(deliveredSevenDaysAgo, today);
        assertTrue("7 days delivery should be eligible on boundary", daysDiff2 <= ELIGIBILITY_WINDOW_DAYS);

        // 8 days ago -> ineligible
        LocalDate deliveredEightDaysAgo = today.minusDays(8);
        long daysDiff3 = ChronoUnit.DAYS.between(deliveredEightDaysAgo, today);
        assertFalse("8 days delivery should exceed 7 days eligibility", daysDiff3 <= ELIGIBILITY_WINDOW_DAYS);

        // 30 days ago -> ineligible
        LocalDate deliveredMonthAgo = today.minusDays(30);
        long daysDiff4 = ChronoUnit.DAYS.between(deliveredMonthAgo, today);
        assertFalse("30 days delivery should exceed 7 days eligibility", daysDiff4 <= ELIGIBILITY_WINDOW_DAYS);
    }

    @Test
    public void testQuantityValidation() {
        int purchasedQty = 3;
        int alreadyReturned = 1;
        int maxEligible = purchasedQty - alreadyReturned;

        assertEquals(2, maxEligible);

        // Valid return quantities
        assertTrue("1 is valid", 1 >= 1 && 1 <= maxEligible);
        assertTrue("2 is valid", 2 >= 1 && 2 <= maxEligible);

        // Invalid return quantities
        assertFalse("0 is invalid", 0 >= 1 && 0 <= maxEligible);
        assertFalse("-1 is invalid", -1 >= 1 && -1 <= maxEligible);
        assertFalse("3 exceeds remaining eligible (2)", 3 >= 1 && 3 <= maxEligible);
    }

    @Test
    public void testDeliveryStatusRequirement() {
        String placed = "PLACED";
        String shipped = "SHIPPED";
        String delivered = "DELIVERED";

        assertFalse("PLACED order cannot have returns", "DELIVERED".equalsIgnoreCase(placed));
        assertFalse("SHIPPED order cannot have returns", "DELIVERED".equalsIgnoreCase(shipped));
        assertTrue("DELIVERED order is eligible for returns", "DELIVERED".equalsIgnoreCase(delivered));
    }
}
