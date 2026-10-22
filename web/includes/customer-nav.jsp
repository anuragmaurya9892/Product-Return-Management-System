<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<nav class="navbar">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/${sessionScope.user.role == 'ADMIN' ? 'admin/dashboard' : 'customer/dashboard'}" class="brand-logo">
            <span>📦</span> ReturnHub
            <span class="brand-badge"><c:out value="${sessionScope.user.role == 'ADMIN' ? 'Admin' : 'Customer'}"/></span>
        </a>
        <ul class="nav-menu">
            <c:choose>
                <c:when test="${sessionScope.user.role == 'ADMIN'}">
                    <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Admin Panel</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link ${activePage == 'products' ? 'active' : ''}">Storefront</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">Manage Orders</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/customer/dashboard" class="nav-link ${activePage == 'dashboard' ? 'active' : ''}">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link ${activePage == 'products' ? 'active' : ''}">Shop Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/orders" class="nav-link ${activePage == 'orders' ? 'active' : ''}">My Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/returns" class="nav-link ${activePage == 'returns' ? 'active' : ''}">My Returns</a></li>
                    <li><a href="${pageContext.request.contextPath}/customer/profile" class="nav-link ${activePage == 'profile' ? 'active' : ''}">Profile</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
        <div class="nav-user">
            <c:if test="${sessionScope.user.role != 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/customer/cart"
                   class="cart-nav-link ${activePage == 'cart' ? 'active' : ''}"
                   title="View Shopping Cart">
                    <span>🛒 Cart</span>
                    <span class="cart-badge-count">
                        <c:out value="${sessionScope.cartCount != null ? sessionScope.cartCount : 0}"/>
                    </span>
                </a>
            </c:if>
            <span class="user-badge">👤 <c:out value="${sessionScope.user.fullName}"/></span>
            <a href="${pageContext.request.contextPath}/login?action=logout" class="btn btn-outline btn-sm">Logout</a>
        </div>
    </div>
</nav>
