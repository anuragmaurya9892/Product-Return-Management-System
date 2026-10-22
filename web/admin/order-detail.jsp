<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Order ${order.orderNumber}" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content" style="max-width: 900px;">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline btn-sm">← Back to Orders</a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <div class="card-header" style="flex-wrap: wrap; gap: 1rem;">
                <div>
                    <h1 class="card-title">Order #<c:out value="${order.orderNumber}"/></h1>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">
                        Placed on <c:out value="${order.orderDate}"/> by
                        <strong><c:out value="${order.user.fullName}"/></strong> (<c:out value="${order.user.email}"/>)
                    </span>
                </div>
                <div>
                    <span class="badge badge-${order.status.toLowerCase()}" style="font-size: 0.85rem; padding: 0.4rem 0.8rem;">
                        <c:out value="${order.status}"/>
                    </span>
                </div>
            </div>

            <!-- Admin Fulfilment Action Bar -->
            <c:if test="${not empty nextStatuses}">
                <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-md); padding: 1.15rem 1.25rem; margin-bottom: 1.75rem; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem;">
                    <div>
                        <div style="font-weight: 700; color: var(--navy); font-size: 0.95rem;">Warehouse &amp; Fulfilment Actions</div>
                        <div style="font-size: 0.82rem; color: var(--text-muted);">
                            Marking an order as <strong>DELIVERED</strong> stamps today's delivery date and activates the customer's 7-day return window.
                        </div>
                    </div>
                    <div style="display: flex; gap: 0.6rem; flex-wrap: wrap;">
                        <c:forEach var="st" items="${nextStatuses}">
                            <form action="${pageContext.request.contextPath}/admin/orders" method="POST" style="display: inline;">
                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <input type="hidden" name="newStatus" value="${st}">
                                <c:choose>
                                    <c:when test="${st == 'SHIPPED'}">
                                        <button type="submit" class="btn btn-primary btn-sm">
                                            🚚 Dispatch &amp; Mark Shipped
                                        </button>
                                    </c:when>
                                    <c:when test="${st == 'DELIVERED'}">
                                        <button type="submit" class="btn btn-success btn-sm">
                                            ✓ Mark as Delivered
                                        </button>
                                    </c:when>
                                    <c:when test="${st == 'CANCELLED'}">
                                        <button type="submit" class="btn btn-danger btn-sm"
                                                data-confirm="Cancel Order #${order.orderNumber} and restore stock to inventory?">
                                            ✕ Cancel Order &amp; Restock
                                        </button>
                                    </c:when>
                                </c:choose>
                            </form>
                        </c:forEach>
                    </div>
                </div>
            </c:if>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Total Amount</div>
                    <div style="font-size: 1.5rem; font-weight: 700; color: var(--navy);">₹<c:out value="${order.totalAmount}"/></div>
                    <div style="font-size: 0.78rem; color: var(--text-muted);">
                        Shipping: <c:out value="${order.shippingFee != null && order.shippingFee > 0 ? '₹'.concat(order.shippingFee) : 'FREE'}"/>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Payment</div>
                    <div style="font-size: 1rem; font-weight: 700; color: var(--navy); margin-top: 0.2rem;">
                        <c:out value="${order.paymentMethod != null ? order.paymentMethod : 'COD'}"/>
                        <span class="badge ${order.paymentStatus == 'PAID' ? 'badge-paid' : 'badge-pending'}" style="margin-left: 0.3rem;">
                            <c:out value="${order.paymentStatus != null ? order.paymentStatus : 'PENDING'}"/>
                        </span>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Shipped / Delivered</div>
                    <div style="font-size: 0.95rem; font-weight: 600; color: var(--navy); margin-top: 0.2rem;">
                        Shipped: <c:out value="${not empty order.shippedDate ? order.shippedDate : '—'}"/><br>
                        Delivered: <c:out value="${not empty order.deliveredDate ? order.deliveredDate : 'Not delivered yet'}"/>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Shipping Address</div>
                    <div style="font-size: 0.92rem; color: var(--text-main); margin-top: 0.2rem;"><c:out value="${order.shippingAddress}"/></div>
                </div>
            </div>

            <h3 style="font-size: 1.15rem; color: var(--navy); margin-bottom: 1rem;">Line Items</h3>
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
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
