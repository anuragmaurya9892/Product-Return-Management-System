<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Order ${order.orderNumber}" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/customer/orders" class="btn btn-outline btn-sm">← Back to Orders</a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <!-- Order Summary Card -->
        <div class="card">
            <div class="card-header" style="flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h1 class="card-title" style="margin-bottom: 0.25rem;">Order #<c:out value="${order.orderNumber}"/></h1>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">Placed on <c:out value="${order.orderDate}"/></span>
                </div>
                <div style="display: flex; align-items: center; gap: 0.75rem; flex-wrap: wrap;">
                    <span class="badge badge-${order.status.toLowerCase()}" style="font-size: 0.85rem; padding: 0.4rem 0.8rem;">
                        <c:out value="${order.status}"/>
                    </span>
                    <c:if test="${canCancel}">
                        <form action="${pageContext.request.contextPath}/customer/order" method="POST" style="display: inline;">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="action" value="cancel">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <button type="submit" class="btn btn-danger btn-sm"
                                    data-confirm="Are you sure you want to cancel Order #${order.orderNumber}? Reserved stock will be released immediately.">
                                ✕ Cancel Order
                            </button>
                        </form>
                    </c:if>
                </div>
            </div>

            <!-- Visual Order Fulfilment Stepper -->
            <c:choose>
                <c:when test="${order.status == 'CANCELLED'}">
                    <div class="alert alert-danger" style="margin-bottom: 1.5rem;">
                        <div>
                            <strong>✕ Order Cancelled</strong> — This order was cancelled before delivery and any reserved inventory was restored.
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="order-stepper">
                        <div class="order-step ${order.status == 'PLACED' ? 'current' : 'completed'}">
                            <div class="order-step-circle">✓</div>
                            <div class="order-step-label">Order Placed</div>
                            <div class="order-step-sub"><c:out value="${order.orderDate}"/></div>
                        </div>
                        <div class="order-step ${order.status == 'SHIPPED' ? 'current' : (order.status == 'DELIVERED' ? 'completed' : '')}">
                            <div class="order-step-circle">${order.status == 'DELIVERED' || order.status == 'SHIPPED' ? '✓' : '2'}</div>
                            <div class="order-step-label">Shipped</div>
                            <div class="order-step-sub">
                                <c:choose>
                                    <c:when test="${not empty order.shippedDate}"><c:out value="${order.shippedDate}"/></c:when>
                                    <c:when test="${order.status == 'SHIPPED' || order.status == 'DELIVERED'}">Dispatched</c:when>
                                    <c:otherwise>Preparing</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="order-step ${order.status == 'DELIVERED' ? 'completed' : ''}">
                            <div class="order-step-circle">${order.status == 'DELIVERED' ? '✓' : '3'}</div>
                            <div class="order-step-label">Delivered</div>
                            <div class="order-step-sub">
                                <c:choose>
                                    <c:when test="${not empty order.deliveredDate}"><c:out value="${order.deliveredDate}"/></c:when>
                                    <c:otherwise>Pending arrival</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; margin-bottom: 0.5rem;">
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Total Amount</div>
                    <div style="font-size: 1.5rem; font-weight: 700; color: var(--navy);">₹<c:out value="${order.totalAmount}"/></div>
                    <div style="font-size: 0.78rem; color: var(--text-muted);">
                        Shipping: <c:out value="${order.shippingFee != null && order.shippingFee > 0 ? '₹'.concat(order.shippingFee) : 'FREE'}"/>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Payment</div>
                    <div style="font-size: 1rem; font-weight: 700; color: var(--navy); margin-top: 0.15rem;">
                        <c:out value="${order.paymentMethod == 'ONLINE_DEMO' ? 'Online Pay' : 'Cash on Delivery'}"/>
                        <span class="badge ${order.paymentStatus == 'PAID' ? 'badge-paid' : (order.paymentStatus == 'VOID' ? 'badge-cancelled' : 'badge-pending')}" style="margin-left: 0.35rem;">
                            <c:out value="${order.paymentStatus != null ? order.paymentStatus : 'PENDING'}"/>
                        </span>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Return Policy Window</div>
                    <div style="font-size: 1.05rem; font-weight: 600; margin-top: 0.15rem;">
                        <c:choose>
                            <c:when test="${withinReturnWindow}">
                                <span style="color: var(--success);">Eligible (${7 - daysSinceDelivery} days remaining)</span>
                            </c:when>
                            <c:when test="${isDelivered}">
                                <span style="color: var(--danger);">Window expired (${daysSinceDelivery} days since delivery)</span>
                            </c:when>
                            <c:when test="${order.status == 'CANCELLED'}">
                                <span style="color: var(--text-muted);">Order cancelled</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: var(--text-muted);">Unlocks on delivery date</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Shipping Address</div>
                    <div style="font-size: 0.92rem; color: var(--text-main); margin-top: 0.15rem;"><c:out value="${order.shippingAddress}"/></div>
                </div>
            </div>
        </div>

        <!-- Order Items Card -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Purchased Items</h2>
            </div>

            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>SKU</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Subtotal</th>
                            <th>Return Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${order.items}">
                            <tr>
                                <td>
                                    <strong><c:out value="${item.product.name}"/></strong><br>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${item.product.category}"/></span>
                                </td>
                                <td><code><c:out value="${item.product.sku}"/></code></td>
                                <td><c:out value="${item.quantity}"/></td>
                                <td>₹<c:out value="${item.unitPrice}"/></td>
                                <td><strong>₹<c:out value="${item.subtotal}"/></strong></td>
                                <td>
                                    <c:set var="activeRet" value="${activeReturnMap[item.id]}"/>
                                    <c:choose>
                                        <c:when test="${not empty activeRet}">
                                            <span class="badge badge-${activeRet.status.toLowerCase().replace('_', '-')}">
                                                <c:out value="${activeRet.status.replace('_', ' ')}"/>
                                            </span>
                                            <div style="font-size: 0.75rem; color: var(--text-muted); margin-top: 2px;">
                                                Ref: <c:out value="${activeRet.returnNumber}"/>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: var(--text-muted); font-size: 0.85rem;">No active return</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${eligibleMap[item.id]}">
                                            <a href="${pageContext.request.contextPath}/customer/return/new?orderItemId=${item.id}"
                                               class="btn btn-primary btn-sm">
                                                Request Return
                                            </a>
                                        </c:when>
                                        <c:when test="${not empty activeRet}">
                                            <a href="${pageContext.request.contextPath}/customer/return?id=${activeRet.id}"
                                               class="btn btn-outline btn-sm">
                                                Track Return
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="btn btn-outline btn-sm" disabled style="opacity: 0.5; cursor: not-allowed;">
                                                Not Eligible
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
