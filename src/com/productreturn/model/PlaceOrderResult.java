package com.productreturn.model;

/**
 * Outcome of a checkout attempt.
 */
public class PlaceOrderResult {

    private final boolean success;
    private final int orderId;
    private final String orderNumber;
    private final String errorMessage;

    private PlaceOrderResult(boolean success, int orderId, String orderNumber, String errorMessage) {
        this.success = success;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.errorMessage = errorMessage;
    }

    public static PlaceOrderResult ok(int orderId, String orderNumber) {
        return new PlaceOrderResult(true, orderId, orderNumber, null);
    }

    public static PlaceOrderResult fail(String message) {
        return new PlaceOrderResult(false, -1, null, message);
    }

    public boolean isSuccess() { return success; }
    public int getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public String getErrorMessage() { return errorMessage; }
}
