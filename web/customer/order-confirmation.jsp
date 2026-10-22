<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Order Confirmed - ${order.orderNumber}" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container" style="max-width: 860px;">
        <!-- Confirmation Banner -->
        <div class="card" style="text-align: center; padding: 2.75rem 2rem; border-top: 4px solid var(--success);">
            <div style="width: 68px; height: 68px; border-radius: 50%; background: var(--success-bg); color: var(--success); display: inline-flex; align-items: center; justify-content: center; font-size: 2rem; margin-bottom: 1rem;">
                ✓
            </div>
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.4rem;">Thank You! Your Order is Confirmed</h1>
            <p style="color: var(--text-muted); font-size: 1rem; max-width: 540px; margin: 0 auto 1.5rem;">
                We've received your order <strong>#<c:out value="${order.orderNumber}"/></strong> and our warehouse team is preparing it for dispatch.
            </p>

            <div style="display: flex; justify-content: center; gap: 0.85rem; flex-wrap: wrap;">
                <a href="${pageContext.request.contextPath}/customer/order?id=${order.id}" class="btn btn-primary">
                    📦 Track Order Status
                </a>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">
                    Continue Shopping →
                </a>
            </div>
        </div>

        <!-- Order Progress Stepper -->
        <div class="card">
            <h2 class="card-title" style="margin-bottom: 0.5rem;">Fulfilment Progress</h2>
            <div class="order-stepper">
                <div class="order-step current">
                    <div class="order-step-circle">✓</div>
                    <div class="order-step-label">Order Placed</div>
                    <div class="order-step-sub"><c:out value="${order.orderDate}"/></div>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <div class="order-step-label">Shipped</div>
                    <div class="order-step-sub">Within 24 hrs</div>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">3</div>
                    <div class="order-step-label">Delivered</div>
                    <div class="order-step-sub">Est. <c:out value="${estimatedDelivery}"/></div>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 1.25rem; padding-top: 1rem; border-top: 1px solid var(--border);">
                <div>
                    <div style="font-size: 0.75rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Order Number</div>
                    <div style="font-weight: 700; color: var(--navy);">#<c:out value="${order.orderNumber}"/></div>
                </div>
                <div>
                    <div style="font-size: 0.75rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Payment</div>
                    <div>
                        <strong><c:out value="${order.paymentMethod == 'ONLINE_DEMO' ? 'Online Pay' : 'Cash on Delivery'}"/></strong>
                        <span class="badge ${order.paymentStatus == 'PAID' ? 'badge-paid' : 'badge-pending'}" style="margin-left: 0.35rem;">
                            <c:out value="${order.paymentStatus}"/>
                        </span>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.75rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Estimated Delivery</div>
                    <div style="font-weight: 700; color: var(--success);"><c:out value="${estimatedDelivery}"/></div>
                </div>
                <div>
                    <div style="font-size: 0.75rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Total Amount</div>
                    <div style="font-size: 1.25rem; font-weight: 800; color: var(--navy);">₹<c:out value="${order.totalAmount}"/></div>
                </div>
            </div>
        </div>

        <!-- Itemized Receipt -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Order Summary &amp; Delivery Address</h2>
            </div>

            <div style="margin-bottom: 1.25rem; background: #f8fafc; padding: 1rem; border-radius: var(--radius-sm); border: 1px solid var(--border);">
                <div style="font-size: 0.78rem; text-transform: uppercase; color: var(--text-muted); font-weight: 700; margin-bottom: 0.25rem;">
                    Shipping To
                </div>
                <div style="font-size: 0.95rem; color: var(--navy);"><c:out value="${order.shippingAddress}"/></div>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>SKU</th>
                            <th>Qty</th>
                            <th>Unit Price</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${order.items}">
                            <tr>
                                <td><strong><c:out value="${item.product.name}"/></strong></td>
                                <td><code><c:out value="${item.product.sku}"/></code></td>
                                <td><c:out value="${item.quantity}"/></td>
                                <td>₹<c:out value="${item.unitPrice}"/></td>
                                <td><strong>₹<c:out value="${item.subtotal}"/></strong></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
