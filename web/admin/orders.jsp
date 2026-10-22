<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="All Orders" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Customer Orders</h1>
            <p style="color: var(--text-muted);">Comprehensive record of all orders placed across the store.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Order #</th>
                            <th>Customer</th>
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
                                <td>
                                    <strong><c:out value="${o.user.fullName}"/></strong><br>
                                    <span style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${o.user.email}"/></span>
                                </td>
                                <td><c:out value="${o.orderDate}"/></td>
                                <td><strong>₹<c:out value="${o.totalAmount}"/></strong></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty o.deliveredDate}">
                                            <c:out value="${o.deliveredDate}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span style="color: var(--text-muted);">In transit</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <span class="badge badge-${o.status.toLowerCase()}">
                                        <c:out value="${o.status}"/>
                                    </span>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/admin/orders?id=${o.id}" class="btn btn-outline btn-sm">
                                        View Details
                                    </a>
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
