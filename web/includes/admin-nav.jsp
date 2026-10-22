<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<aside class="admin-sidebar">
    <div>
        <div style="padding: 0 0.5rem 1.5rem 0.5rem; border-bottom: 1px solid var(--border); margin-bottom: 1.25rem;">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="brand-logo">
                <span>🛡️</span> ReturnHub
                <span class="brand-badge" style="background:#dc2626;">Admin</span>
            </a>
        </div>
        <ul class="admin-sidebar-menu">
            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-sidebar-link ${activePage == 'dashboard' ? 'active' : ''}">📊 Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/returns" class="admin-sidebar-link ${activePage == 'returns' ? 'active' : ''}">🔄 Return Requests</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/products" class="admin-sidebar-link ${activePage == 'products' ? 'active' : ''}">📦 Products</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/orders" class="admin-sidebar-link ${activePage == 'orders' ? 'active' : ''}">🛒 Orders</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/customers" class="admin-sidebar-link ${activePage == 'customers' ? 'active' : ''}">👥 Customers</a></li>
            <li><a href="${pageContext.request.contextPath}/admin/reports" class="admin-sidebar-link ${activePage == 'reports' ? 'active' : ''}">📈 Reports & Stats</a></li>
        </ul>
    </div>
    <div style="padding-top: 1.25rem; border-top: 1px solid var(--border);">
        <div style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 0.5rem;">
            Logged in as:<br>
            <strong style="color: var(--navy);"><c:out value="${sessionScope.user.fullName}"/></strong>
        </div>
        <a href="${pageContext.request.contextPath}/login?action=logout" class="btn btn-outline btn-sm btn-block">Logout</a>
    </div>
</aside>
