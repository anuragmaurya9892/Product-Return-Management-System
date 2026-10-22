<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="My Return Requests" scope="request"/>
<c:set var="activePage" value="returns" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">My Returns &amp; Replacements</h1>
            <p style="color: var(--text-muted);">Track the processing stages of your submitted return requests.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <c:choose>
                <c:when test="${empty returns}">
                    <div class="empty-state">
                        <div class="empty-state-icon">📦</div>
                        <h3 class="empty-state-title">No return requests</h3>
                        <p>You have not submitted any return requests. Returns can be initiated from eligible delivered orders.</p>
                        <a href="${pageContext.request.contextPath}/customer/orders" class="btn btn-primary" style="margin-top: 1rem;">
                            View My Orders
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Return Ref #</th>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Reason</th>
                                    <th>Resolution</th>
                                    <th>Status</th>
                                    <th>Submitted On</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="ret" items="${returns}">
                                    <tr>
                                        <td><strong><c:out value="${ret.returnNumber}"/></strong></td>
                                        <td>
                                            <strong><c:out value="${ret.orderItem.product.name}"/></strong><br>
                                            <span style="font-size: 0.8rem; color: var(--text-muted);">SKU: <c:out value="${ret.orderItem.product.sku}"/></span>
                                        </td>
                                        <td><c:out value="${ret.quantity}"/></td>
                                        <td><c:out value="${ret.reason.replace('_', ' ')}"/></td>
                                        <td>
                                            <span style="font-weight: 600; color: ${ret.resolutionType == 'REFUND' ? 'var(--primary)' : 'var(--purple)'};">
                                                <c:out value="${ret.resolutionType}"/>
                                            </span>
                                        </td>
                                        <td>
                                            <span class="badge badge-${ret.status.toLowerCase().replace('_', '-')}">
                                                <c:out value="${ret.status.replace('_', ' ')}"/>
                                            </span>
                                        </td>
                                        <td><c:out value="${ret.createdAt}"/></td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/customer/return?id=${ret.id}" class="btn btn-outline btn-sm">
                                                Track / Details
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
