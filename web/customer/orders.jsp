<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="My Orders" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: flex-end; flex-wrap: wrap; gap: 1rem; margin-bottom: 2rem;">
            <div>
                <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">My Orders</h1>
                <p style="color: var(--text-muted);">Track your purchases, manage active orders, or request a 7-day return on delivered items.</p>
            </div>
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">
                + Place New Order
            </a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <c:choose>
                <c:when test="${empty orders}">
                    <div class="empty-state">
                        <div class="empty-state-icon">🛒</div>
                        <h3 class="empty-state-title">No orders found</h3>
                        <p>You haven't placed any orders yet. Visit our catalogue to find what you need!</p>
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="margin-top: 1rem;">
                            Browse Products
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Order #</th>
                                    <th>Order Date</th>
                                    <th>Total Amount</th>
                                    <th>Payment</th>
                                    <th>Delivery Date</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${orders}">
                                    <tr>
                                        <td><strong><c:out value="${o.orderNumber}"/></strong></td>
                                        <td><c:out value="${o.orderDate}"/></td>
                                        <td><strong>₹<c:out value="${o.totalAmount}"/></strong></td>
                                        <td>
                                            <span style="font-size: 0.82rem; font-weight: 600;"><c:out value="${o.paymentMethod != null ? o.paymentMethod : 'COD'}"/></span>
                                            <span class="badge ${o.paymentStatus == 'PAID' ? 'badge-paid' : 'badge-pending'}" style="margin-left: 0.25rem;">
                                                <c:out value="${o.paymentStatus != null ? o.paymentStatus : 'PENDING'}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty o.deliveredDate}">
                                                    <c:out value="${o.deliveredDate}"/>
                                                </c:when>
                                                <c:when test="${o.status == 'CANCELLED'}">
                                                    <span style="color: var(--danger); font-size: 0.85rem;">Cancelled</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: var(--text-muted);">Pending delivery</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="badge badge-${o.status.toLowerCase()}">
                                                <c:out value="${o.status}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/customer/order?id=${o.id}" class="btn btn-outline btn-sm">
                                                Track / Return
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
