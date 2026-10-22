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

        <div class="card">
            <div class="card-header">
                <div>
                    <h1 class="card-title">Order #<c:out value="${order.orderNumber}"/></h1>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">
                        Placed on <c:out value="${order.orderDate}"/> by
                        <strong><c:out value="${order.user.fullName}"/></strong> (<c:out value="${order.user.email}"/>)
                    </span>
                </div>
                <div>
                    <span class="badge badge-${order.status.toLowerCase()}">
                        <c:out value="${order.status}"/>
                    </span>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Total Amount</div>
                    <div style="font-size: 1.5rem; font-weight: 700; color: var(--navy);">₹<c:out value="${order.totalAmount}"/></div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Delivery Date</div>
                    <div style="font-size: 1.1rem; font-weight: 600; color: var(--navy);">
                        <c:choose>
                            <c:when test="${not empty order.deliveredDate}"><c:out value="${order.deliveredDate}"/></c:when>
                            <c:otherwise>Not delivered yet</c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Shipping Address</div>
                    <div style="font-size: 0.95rem; color: var(--text-main);"><c:out value="${order.shippingAddress}"/></div>
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
