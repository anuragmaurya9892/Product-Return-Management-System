<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Manage Returns" scope="request"/>
<c:set var="activePage" value="returns" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Product Return Requests</h1>
            <p style="color: var(--text-muted);">Inspect, search, filter, and transition return requests through the RMA lifecycle.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <!-- Filter Bar -->
        <form action="${pageContext.request.contextPath}/admin/returns" method="GET" class="filter-bar">
            <div class="filter-item" style="flex: 2 1 200px;">
                <label for="search" class="form-label">Search</label>
                <input type="text" id="search" name="search" class="form-control"
                       placeholder="Return #, Order #, Customer, Product..."
                       value="<c:out value="${search}"/>">
            </div>

            <div class="filter-item">
                <label for="status" class="form-label">Status</label>
                <select id="status" name="status" class="form-select">
                    <option value="ALL">All Statuses</option>
                    <option value="PENDING" ${status == 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="APPROVED" ${status == 'APPROVED' ? 'selected' : ''}>Approved</option>
                    <option value="ITEM_RECEIVED" ${status == 'ITEM_RECEIVED' ? 'selected' : ''}>Item Received</option>
                    <option value="REFUND_PROCESSED" ${status == 'REFUND_PROCESSED' ? 'selected' : ''}>Refund Processed</option>
                    <option value="REPLACEMENT_SHIPPED" ${status == 'REPLACEMENT_SHIPPED' ? 'selected' : ''}>Replacement Shipped</option>
                    <option value="REJECTED" ${status == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                </select>
            </div>

            <div class="filter-item">
                <label for="resolution" class="form-label">Resolution</label>
                <select id="resolution" name="resolution" class="form-select">
                    <option value="ALL">All Types</option>
                    <option value="REFUND" ${resolution == 'REFUND' ? 'selected' : ''}>Refund</option>
                    <option value="REPLACEMENT" ${resolution == 'REPLACEMENT' ? 'selected' : ''}>Replacement</option>
                </select>
            </div>

            <div class="filter-item">
                <label for="dateFrom" class="form-label">Date From</label>
                <input type="date" id="dateFrom" name="dateFrom" class="form-control" value="<c:out value="${dateFrom}"/>">
            </div>

            <div class="filter-item">
                <label for="dateTo" class="form-label">Date To</label>
                <input type="date" id="dateTo" name="dateTo" class="form-control" value="<c:out value="${dateTo}"/>">
            </div>

            <div class="filter-item" style="flex: 0 0 auto;">
                <button type="submit" class="btn btn-primary">Filter</button>
                <a href="${pageContext.request.contextPath}/admin/returns" class="btn btn-outline">Reset</a>
            </div>
        </form>

        <!-- Returns Table -->
        <div class="card">
            <c:choose>
                <c:when test="${empty returns}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📋</div>
                        <h3 class="empty-state-title">No matching return requests</h3>
                        <p>No return records found matching the specified filters.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Return Ref</th>
                                    <th>Customer</th>
                                    <th>Order #</th>
                                    <th>Product</th>
                                    <th>Qty</th>
                                    <th>Resolution</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Date</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="ret" items="${returns}">
                                    <tr>
                                        <td><strong><c:out value="${ret.returnNumber}"/></strong></td>
                                        <td>
                                            <strong><c:out value="${ret.user.fullName}"/></strong><br>
                                            <span style="font-size: 0.8rem; color: var(--text-muted);"><c:out value="${ret.user.email}"/></span>
                                        </td>
                                        <td><c:out value="${ret.orderItem.product.sku}"/></td>
                                        <td><c:out value="${ret.orderItem.product.name}"/></td>
                                        <td><c:out value="${ret.quantity}"/></td>
                                        <td>
                                            <span style="font-weight: 600; color: ${ret.resolutionType == 'REFUND' ? 'var(--primary)' : 'var(--purple)'};">
                                                <c:out value="${ret.resolutionType}"/>
                                            </span>
                                        </td>
                                        <td><c:out value="${ret.reason.replace('_', ' ')}"/></td>
                                        <td>
                                            <span class="badge badge-${ret.status.toLowerCase().replace('_', '-')}">
                                                <c:out value="${ret.status.replace('_', ' ')}"/>
                                            </span>
                                        </td>
                                        <td><c:out value="${ret.createdAt}"/></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/admin/return?id=${ret.id}" class="btn btn-outline btn-sm">
                                                Process
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
