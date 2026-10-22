<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Customer Dashboard" scope="request"/>
<c:set var="activePage" value="dashboard" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <div>
                <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">
                    Hello, <c:out value="${sessionScope.user.fullName}"/> 👋
                </h1>
                <p style="color: var(--text-muted);">Welcome to your ReturnHub account dashboard.</p>
            </div>
            <div style="display: flex; gap: 0.75rem;">
                <a href="${pageContext.request.contextPath}/customer/orders" class="btn btn-outline">My Orders</a>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Catalogue</a>
            </div>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <!-- Stat Cards -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon blue">🛒</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${totalOrders}"/></div>
                    <div class="stat-label">Total Orders</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon purple">🔄</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${totalReturns}"/></div>
                    <div class="stat-label">Return Requests</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon amber">⏳</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${pendingReturns}"/></div>
                    <div class="stat-label">Pending Approval</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon green">✅</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${approvedReturns}"/></div>
                    <div class="stat-label">Approved Returns</div>
                </div>
            </div>
        </div>

        <!-- Recent Returns -->
        <div class="card">
            <div class="card-header">
                <h2 class="card-title">Recent Return Requests</h2>
                <a href="${pageContext.request.contextPath}/customer/returns" class="btn btn-outline btn-sm">View All Returns</a>
            </div>

            <c:choose>
                <c:when test="${empty recentReturns}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📋</div>
                        <h3 class="empty-state-title">No return requests yet</h3>
                        <p>Have an issue with a delivered item? You can submit a return request from your Orders page.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Return Ref</th>
                                    <th>Product</th>
                                    <th>Reason</th>
                                    <th>Resolution</th>
                                    <th>Status</th>
                                    <th>Date</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="ret" items="${recentReturns}">
                                    <tr>
                                        <td><strong><c:out value="${ret.returnNumber}"/></strong></td>
                                        <td><c:out value="${ret.orderItem.product.name}"/></td>
                                        <td><c:out value="${ret.reason}"/></td>
                                        <td><c:out value="${ret.resolutionType}"/></td>
                                        <td>
                                            <span class="badge badge-${ret.status.toLowerCase().replace('_', '-')}">
                                                <c:out value="${ret.status.replace('_', ' ')}"/>
                                            </span>
                                        </td>
                                        <td><c:out value="${ret.createdAt}"/></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/customer/return?id=${ret.id}" class="btn btn-outline btn-sm">
                                                Details
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
