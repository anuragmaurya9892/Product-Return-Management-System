<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Admin Dashboard" scope="request"/>
<c:set var="activePage" value="dashboard" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Operations Overview</h1>
            <p style="color: var(--text-muted);">Real-time monitoring of return authorizations, refunds, orders, and customer activity.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <!-- Stat Cards Grid -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon blue">🛒</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${totalOrders}"/></div>
                    <div class="stat-label">Total Orders</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon amber">⏳</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${pendingReturns}"/></div>
                    <div class="stat-label">Pending Returns</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon green">✅</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${approvedReturns}"/></div>
                    <div class="stat-label">Approved Returns</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon red">❌</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${rejectedReturns}"/></div>
                    <div class="stat-label">Rejected Returns</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon purple">💳</div>
                <div class="stat-info">
                    <div class="stat-value">₹<c:out value="${totalRefundAmount}"/></div>
                    <div class="stat-label">Processed Refunds (${processedRefundsCount})</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon blue">👥</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${totalCustomers}"/></div>
                    <div class="stat-label">Active Customers</div>
                </div>
            </div>
        </div>

        <!-- Pending Returns Requiring Action -->
        <div class="card">
            <div class="card-header">
                <div>
                    <h2 class="card-title">Pending Requests Awaiting Review</h2>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">Review and approve or reject submissions</span>
                </div>
                <a href="${pageContext.request.contextPath}/admin/returns?status=PENDING" class="btn btn-outline btn-sm">View All Pending</a>
            </div>

            <c:choose>
                <c:when test="${empty pendingList}">
                    <div class="empty-state">
                        <div class="empty-state-icon">🎉</div>
                        <h3 class="empty-state-title">All caught up!</h3>
                        <p>There are no pending return requests awaiting administrator review right now.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Return Ref</th>
                                    <th>Customer</th>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Reason</th>
                                    <th>Resolution</th>
                                    <th>Submitted On</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="ret" items="${pendingList}">
                                    <tr>
                                        <td><strong><c:out value="${ret.returnNumber}"/></strong></td>
                                        <td>
                                            <strong><c:out value="${ret.user.fullName}"/></strong><br>
                                            <span style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${ret.user.email}"/></span>
                                        </td>
                                        <td><c:out value="${ret.orderItem.product.name}"/></td>
                                        <td><c:out value="${ret.quantity}"/></td>
                                        <td><c:out value="${ret.reason.replace('_', ' ')}"/></td>
                                        <td>
                                            <span style="font-weight: 600; color: ${ret.resolutionType == 'REFUND' ? 'var(--primary)' : 'var(--purple)'};">
                                                <c:out value="${ret.resolutionType}"/>
                                            </span>
                                        </td>
                                        <td><c:out value="${ret.createdAt}"/></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/return?id=${ret.id}" class="btn btn-primary btn-sm">
                                                Review &amp; Process
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
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
