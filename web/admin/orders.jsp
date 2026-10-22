<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="All Orders" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="margin-bottom: 1.5rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Customer Orders &amp; Fulfilment</h1>
            <p style="color: var(--text-muted);">Dispatch placed orders, mark shipments as delivered (which unlocks the 7-day return window), or cancel unshipped orders.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <!-- Status Filter Tabs -->
        <div class="status-tabs">
            <a href="${pageContext.request.contextPath}/admin/orders"
               class="status-tab ${empty selectedStatus || selectedStatus == 'ALL' ? 'active' : ''}">All Orders</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=PLACED"
               class="status-tab ${selectedStatus == 'PLACED' ? 'active' : ''}">Placed (Awaiting Dispatch)</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=SHIPPED"
               class="status-tab ${selectedStatus == 'SHIPPED' ? 'active' : ''}">Shipped (In Transit)</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=DELIVERED"
               class="status-tab ${selectedStatus == 'DELIVERED' ? 'active' : ''}">Delivered</a>
            <a href="${pageContext.request.contextPath}/admin/orders?status=CANCELLED"
               class="status-tab ${selectedStatus == 'CANCELLED' ? 'active' : ''}">Cancelled</a>
        </div>

        <div class="card">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Order #</th>
                            <th>Customer</th>
                            <th>Order Date</th>
                            <th>Total</th>
                            <th>Payment</th>
                            <th>Delivery Date</th>
                            <th>Status</th>
                            <th>Quick Fulfilment / Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td><strong><c:out value="${o.orderNumber}"/></strong></td>
                                <td>
                                    <strong><c:out value="${o.user.fullName}"/></strong><br>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${o.user.email}"/></span>
                                </td>
                                <td><c:out value="${o.orderDate}"/></td>
                                <td><strong>₹<c:out value="${o.totalAmount}"/></strong></td>
                                <td>
                                    <span style="font-size: 0.8rem; font-weight: 600;"><c:out value="${o.paymentMethod != null ? o.paymentMethod : 'COD'}"/></span>
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
                                            <span style="color: var(--text-muted);">In transit / pending</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="badge badge-${o.status.toLowerCase()}">
                                        <c:out value="${o.status}"/>
                                    </span>
                                </td>
                                <td>
                                    <div style="display: flex; gap: 0.4rem; flex-wrap: wrap;">
                                        <c:if test="${o.status == 'PLACED'}">
                                            <form action="${pageContext.request.contextPath}/admin/orders" method="POST" style="display: inline;">
                                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="orderId" value="${o.id}">
                                                <input type="hidden" name="newStatus" value="SHIPPED">
                                                <input type="hidden" name="fromList" value="1">
                                                <button type="submit" class="btn btn-primary btn-sm">
                                                    🚚 Mark Shipped
                                                </button>
                                            </form>
                                        </c:if>
                                        <c:if test="${o.status == 'SHIPPED'}">
                                            <form action="${pageContext.request.contextPath}/admin/orders" method="POST" style="display: inline;">
                                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="orderId" value="${o.id}">
                                                <input type="hidden" name="newStatus" value="DELIVERED">
                                                <input type="hidden" name="fromList" value="1">
                                                <button type="submit" class="btn btn-success btn-sm">
                                                    ✓ Mark Delivered
                                                </button>
                                            </form>
                                        </c:if>
                                        <a href="${pageContext.request.contextPath}/admin/orders?id=${o.id}" class="btn btn-outline btn-sm">
                                            Details
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
