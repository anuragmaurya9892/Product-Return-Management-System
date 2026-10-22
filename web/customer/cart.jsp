<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Shopping Cart" scope="request"/>
<c:set var="activePage" value="cart" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: flex-end; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.5rem;">
            <div>
                <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.25rem;">Your Shopping Cart</h1>
                <p style="color: var(--text-muted);">Review your items and adjust quantities before checkout.</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">
                ← Continue Shopping
            </a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="card empty-state" style="padding: 4rem 1.5rem;">
                    <div class="empty-state-icon">🛒</div>
                    <h3 class="empty-state-title">Your cart is empty</h3>
                    <p style="max-width: 440px; margin: 0 auto 1.5rem;">
                        Explore our catalogue and add products to your cart. All orders over ₹<c:out value="${freeShippingThreshold}"/> ship free!
                    </p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                        Browse Products →
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <!-- Free Shipping Progress Bar -->
                <div class="shipping-progress-card">
                    <div style="display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 0.5rem;">
                        <div>
                            <c:choose>
                                <c:when test="${amountToFreeShipping <= 0}">
                                    <span style="font-weight: 700; color: var(--success);">
                                        🎉 Congratulations! Your order qualifies for FREE Express Shipping!
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span style="font-weight: 600; color: var(--navy);">
                                        🚚 Add <strong>₹<c:out value="${amountToFreeShipping}"/></strong> more to unlock <strong style="color: var(--success);">FREE Shipping</strong>!
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <span style="font-size: 0.8rem; font-weight: 700; color: var(--primary);">
                            <c:out value="${freeShippingProgress}"/>% of ₹<c:out value="${freeShippingThreshold}"/>
                        </span>
                    </div>
                    <div class="shipping-progress-track">
                        <div class="shipping-progress-bar" style="width: ${freeShippingProgress}%;"></div>
                    </div>
                </div>

                <div class="commerce-grid">
                    <!-- Left Column: Cart Items -->
                    <div class="card" style="margin-bottom: 0;">
                        <div class="card-header">
                            <h2 class="card-title">Cart Items (<c:out value="${sessionScope.cartCount}"/> units)</h2>
                            <form action="${pageContext.request.contextPath}/customer/cart" method="POST">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn btn-outline btn-sm"
                                        data-confirm="Remove all items from your shopping cart?">
                                    Clear Cart
                                </button>
                            </form>
                        </div>

                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th>Product</th>
                                        <th>Unit Price</th>
                                        <th>Quantity</th>
                                        <th>Line Total</th>
                                        <th></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="item" items="${cartItems}">
                                        <tr>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/product?id=${item.product.id}"
                                                   style="font-weight: 700; color: var(--navy);">
                                                    <c:out value="${item.product.name}"/>
                                                </a>
                                                <div style="font-size: 0.78rem; color: var(--text-muted);">
                                                    <c:out value="${item.product.category}"/> • SKU: <code><c:out value="${item.product.sku}"/></code>
                                                </div>
                                                <c:choose>
                                                    <c:when test="${!item.available}">
                                                        <span class="stock-pill low-stock" style="margin-top: 4px;">
                                                            ⚠ Only <c:out value="${item.product.stockQuantity}"/> in stock
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span style="font-size: 0.75rem; color: var(--success); font-weight: 600;">
                                                            ✔ In Stock
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>₹<c:out value="${item.product.price}"/></td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/customer/cart" method="POST">
                                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                    <input type="hidden" name="action" value="update">
                                                    <input type="hidden" name="productId" value="${item.product.id}">
                                                    <div class="qty-stepper" data-autosubmit="true">
                                                        <button type="button" class="qty-btn" data-qty-minus aria-label="Decrease">−</button>
                                                        <input type="number" name="quantity" class="qty-input"
                                                               value="${item.quantity}" min="1"
                                                               max="${item.product.stockQuantity < 10 ? item.product.stockQuantity : 10}"
                                                               readonly>
                                                        <button type="button" class="qty-btn" data-qty-plus aria-label="Increase">+</button>
                                                    </div>
                                                </form>
                                            </td>
                                            <td><strong style="color: var(--navy);">₹<c:out value="${item.lineTotal}"/></strong></td>
                                            <td style="text-align: right;">
                                                <form action="${pageContext.request.contextPath}/customer/cart" method="POST">
                                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="productId" value="${item.product.id}">
                                                    <button type="submit" class="btn btn-outline btn-sm" style="color: var(--danger);" title="Remove item">
                                                        ✕ Remove
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Right Column: Sticky Order Summary -->
                    <div class="card summary-sticky" style="margin-bottom: 0;">
                        <div class="card-header">
                            <h2 class="card-title">Order Summary</h2>
                        </div>

                        <div class="summary-row">
                            <span>Subtotal (<c:out value="${sessionScope.cartCount}"/> items)</span>
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
                            <span>Grand Total</span>
                            <span>₹<c:out value="${grandTotal}"/></span>
                        </div>

                        <div style="margin-top: 1.5rem;">
                            <a href="${pageContext.request.contextPath}/customer/checkout" class="btn btn-primary btn-block" style="padding: 0.85rem; font-size: 1rem;">
                                Proceed to Checkout →
                            </a>
                        </div>

                        <div style="margin-top: 1.25rem; padding-top: 1rem; border-top: 1px solid var(--border); font-size: 0.8rem; color: var(--text-muted);">
                            <div style="margin-bottom: 0.35rem;">🔒 <strong>Secure Checkout</strong> — Stock is reserved atomically when you place your order.</div>
                            <div>🛡️ <strong>7-Day Return Policy</strong> — Easy replacement or refund on all delivered items.</div>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
