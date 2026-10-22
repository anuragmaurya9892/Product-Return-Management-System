<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Reports & Analytics" scope="request"/>
<c:set var="activePage" value="reports" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Return Analytics &amp; Reports</h1>
            <p style="color: var(--text-muted);">Aggregated statistics on return reasons, resolution preferences, and refund financial liabilities.</p>
        </div>

        <!-- Summary KPIs -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon purple">📊</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${totalReturns}"/></div>
                    <div class="stat-label">Total Return Cases</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon green">💰</div>
                <div class="stat-info">
                    <div class="stat-value">₹<c:out value="${totalRefundAmount}"/></div>
                    <div class="stat-label">Total Refunded Amount</div>
                </div>
            </div>

            <div class="stat-card">
                <div class="stat-icon blue">🧾</div>
                <div class="stat-info">
                    <div class="stat-value"><c:out value="${processedRefundsCount}"/></div>
                    <div class="stat-label">Processed Refunds</div>
                </div>
            </div>
        </div>

        <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(360px, 1fr)); gap: 1.5rem;">
            <!-- Returns by Reason -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Returns by Reason</h2>
                </div>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Reason</th>
                                <th>Count</th>
                                <th>Share</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="entry" items="${returnsByReason}">
                                <tr>
                                    <td><strong><c:out value="${entry.key.replace('_', ' ')}"/></strong></td>
                                    <td><c:out value="${entry.value}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${totalReturns > 0}">
                                                <div style="display: flex; align-items: center; gap: 0.5rem;">
                                                    <div style="flex: 1; height: 8px; background: #e2e8f0; border-radius: 4px; overflow: hidden;">
                                                        <div style="width: ${(entry.value * 100) / totalReturns}%; height: 100%; background: var(--primary);"></div>
                                                    </div>
                                                    <span style="font-size: 0.8rem; color: var(--text-muted); width: 40px;">
                                                        ${Math.round((entry.value * 100) / totalReturns)}%
                                                    </span>
                                                </div>
                                            </c:when>
                                            <c:otherwise>0%</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Returns by Status -->
            <div class="card">
                <div class="card-header">
                    <h2 class="card-title">Returns by Status</h2>
                </div>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Status</th>
                                <th>Count</th>
                                <th>Badge</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="entry" items="${returnsByStatus}">
                                <tr>
                                    <td><strong><c:out value="${entry.key.replace('_', ' ')}"/></strong></td>
                                    <td><c:out value="${entry.value}"/></td>
                                    <td>
                                        <span class="badge badge-${entry.key.toLowerCase().replace('_', '-')}">
                                            <c:out value="${entry.key.replace('_', ' ')}"/>
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Resolution Preferences -->
            <div class="card" style="grid-column: 1 / -1;">
                <div class="card-header">
                    <h2 class="card-title">Customer Resolution Preferences</h2>
                </div>
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Resolution Type</th>
                                <th>Total Count</th>
                                <th>Percentage of All Returns</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="entry" items="${returnsByResolution}">
                                <tr>
                                    <td><strong><c:out value="${entry.key}"/></strong></td>
                                    <td><c:out value="${entry.value}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${totalReturns > 0}">
                                                ${Math.round((entry.value * 100) / totalReturns)}%
                                            </c:when>
                                            <c:otherwise>0%</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
