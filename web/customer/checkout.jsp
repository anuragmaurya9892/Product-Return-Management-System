<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Checkout" scope="request"/>
<c:set var="activePage" value="cart" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="margin-bottom: 1.25rem;">
            <a href="${pageContext.request.contextPath}/customer/cart" class="btn btn-outline btn-sm">← Back to Cart</a>
        </div>

        <div style="margin-bottom: 1.75rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.25rem;">Secure Checkout</h1>
            <p style="color: var(--text-muted);">Confirm your delivery address and select a payment method to place your order.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <form action="${pageContext.request.contextPath}/customer/checkout" method="POST">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <div class="commerce-grid">
                <!-- Left Column: Delivery & Payment -->
                <div>
                    <!-- Step 1: Delivery Details -->
                    <div class="card">
                        <div class="card-header">
                            <h2 class="card-title">1. Delivery Address</h2>
                        </div>

                        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1rem;">
                            <div class="form-group">
                                <label for="fullName" class="form-label">Recipient Full Name *</label>
                                <input type="text" id="fullName" name="fullName" class="form-control"
                                       required maxlength="100"
                                       value="<c:out value="${fullName}"/>">
                            </div>

                            <div class="form-group">
                                <label for="phone" class="form-label">Mobile / Contact Number *</label>
                                <input type="text" id="phone" name="phone" class="form-control"
                                       required maxlength="20" placeholder="+91 98765 43210"
                                       value="<c:out value="${phone}"/>">
                            </div>
                        </div>

                        <div class="form-group" style="margin-bottom: 0;">
                            <label for="address" class="form-label">Complete Street Address, City, State &amp; PIN *</label>
                            <textarea id="address" name="address" class="form-control"
                                      required minlength="10" maxlength="500"
                                      placeholder="Flat / House No., Building, Street, City, State - PIN Code"><c:out value="${address}"/></textarea>
                            <div class="form-help">Please enter at least 10 characters including landmark and PIN code.</div>
                        </div>
                    </div>

                    <!-- Step 2: Payment Method -->
                    <div class="card" style="margin-bottom: 0;">
                        <div class="card-header">
                            <h2 class="card-title">2. Payment Method</h2>
                        </div>

                        <div class="payment-options">
                            <label class="payment-card ${paymentMethod != 'ONLINE_DEMO' ? 'selected' : ''}">
                                <input type="radio" name="paymentMethod" value="COD"
                                       ${paymentMethod != 'ONLINE_DEMO' ? 'checked' : ''}>
                                <div class="payment-card-title">💵 Cash on Delivery (COD)</div>
                                <div class="payment-card-desc">
                                    Pay via cash or UPI at your doorstep when your package is delivered.
                                </div>
                            </label>

                            <label class="payment-card ${paymentMethod == 'ONLINE_DEMO' ? 'selected' : ''}">
                                <input type="radio" name="paymentMethod" value="ONLINE_DEMO"
                                       ${paymentMethod == 'ONLINE_DEMO' ? 'checked' : ''}>
                                <div class="payment-card-title">⚡ Instant Online Pay (Demo)</div>
                                <div class="payment-card-desc">
                                    Instant simulated card / UPI payment. Marks your order as <strong>PAID</strong> immediately.
                                </div>
                            </label>
                        </div>
                    </div>
                </div>

                <!-- Right Column: Order Review & Submit -->
                <div class="card summary-sticky" style="margin-bottom: 0;">
                    <div class="card-header">
                        <h2 class="card-title">Order Review</h2>
                    </div>

                    <div style="max-height: 260px; overflow-y: auto; margin-bottom: 1rem; padding-right: 0.25rem;">
                        <c:forEach var="item" items="${cartItems}">
                            <div style="display: flex; justify-content: space-between; align-items: flex-start; gap: 0.75rem; padding: 0.65rem 0; border-bottom: 1px solid var(--border);">
                                <div>
                                    <div style="font-weight: 600; font-size: 0.9rem; color: var(--navy);">
                                        <c:out value="${item.product.name}"/>
                                    </div>
                                    <div style="font-size: 0.78rem; color: var(--text-muted);">
                                        Qty: <c:out value="${item.quantity}"/> × ₹<c:out value="${item.product.price}"/>
                                    </div>
                                </div>
                                <strong style="font-size: 0.9rem; color: var(--navy);">₹<c:out value="${item.lineTotal}"/></strong>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="summary-row">
                        <span>Items Subtotal</span>
                        <strong>₹<c:out value="${subtotal}"/></strong>
                    </div>
                    <div class="summary-row">
                        <span>Express Shipping</span>
                        <c:choose>
                            <c:when test="${shippingFee == 0}">
                                <strong style="color: var(--success);">FREE</strong>
                            </c:when>
                            <c:otherwise>
                                <strong>₹<c:out value="${shippingFee}"/></strong>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="summary-row total">
                        <span>Total Payable</span>
                        <span>₹<c:out value="${grandTotal}"/></span>
                    </div>

                    <div style="margin-top: 1.5rem;">
                        <button type="submit" class="btn btn-success btn-block" style="padding: 0.9rem; font-size: 1.05rem;">
                            ✓ Place Order (₹<c:out value="${grandTotal}"/>)
                        </button>
                    </div>

                    <div style="margin-top: 1rem; font-size: 0.78rem; color: var(--text-muted); text-align: center;">
                        By placing this order, you agree to ReturnHub's 7-day return &amp; replacement policy.
                    </div>
                </div>
            </div>
        </form>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
