<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<nav class="navbar">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/customer/dashboard" class="brand-logo">
            <span>📦</span> ReturnHub
            <span class="brand-badge">Customer</span>
        </a>
        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/customer/dashboard" class="nav-link ${activePage == 'dashboard' ? 'active' : ''}">Dashboard</a></li>
            <li><a href="${pageContext.request.contextPath}/products" class="nav-link ${activePage == 'products' ? 'active' : ''}">Products</a></li>
            <li><a href="${pageContext.request.contextPath}/customer/orders" class="nav-link ${activePage == 'orders' ? 'active' : ''}">My Orders</a></li>
            <li><a href="${pageContext.request.contextPath}/customer/returns" class="nav-link ${activePage == 'returns' ? 'active' : ''}">My Returns</a></li>
            <li><a href="${pageContext.request.contextPath}/customer/profile" class="nav-link ${activePage == 'profile' ? 'active' : ''}">Profile</a></li>
        </ul>
        <div class="nav-user">
            <span class="user-badge">👤 <c:out value="${sessionScope.user.fullName}"/></span>
            <a href="${pageContext.request.contextPath}/login?action=logout" class="btn btn-outline btn-sm">Logout</a>
        </div>
    </div>
</nav>
