<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="My Orders" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">My Orders</h1>
            <p style="color: var(--text-muted);">View all your past purchases and initiate return requests on eligible delivered items.</p>
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
                                            <c:choose>
                                                <c:when test="${not empty o.deliveredDate}">
                                                    <c:out value="${o.deliveredDate}"/>
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
                                                View Items &amp; Return
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
