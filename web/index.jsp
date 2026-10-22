<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Home" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<nav class="navbar">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/" class="brand-logo">
            <span>📦</span> ReturnHub
        </a>
        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'ADMIN'}">
                            <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary btn-sm">Admin Dashboard</a></li>
                        </c:when>
                        <c:otherwise>
                            <li><a href="${pageContext.request.contextPath}/customer/dashboard" class="btn btn-primary btn-sm">My Dashboard</a></li>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <li><a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a></li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>

<section class="hero-section">
    <div class="container">
        <h1 class="hero-title">Hassle-Free Product Returns &amp; Replacements</h1>
        <p class="hero-subtitle">
            A transparent, automated return management system for modern ecommerce.
            Request returns in seconds, upload evidence, and track your refunds in real time.
        </p>
        <div class="hero-actions">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="padding: 0.85rem 1.75rem; font-size: 1.05rem;">Browse Products</a>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline" style="padding: 0.85rem 1.75rem; font-size: 1.05rem; color: white; border-color: rgba(255,255,255,0.4);">Customer Login</a>
        </div>
    </div>
</section>

<main class="container" style="padding: 4rem 1.25rem;">
    <h2 style="text-align: center; font-size: 2rem; color: var(--navy); margin-bottom: 0.5rem;">Why ReturnHub?</h2>
    <p style="text-align: center; color: var(--text-muted); margin-bottom: 2rem;">Designed for customer satisfaction and efficient operations</p>

    <div class="features-grid" style="margin-top: 0;">
        <div class="feature-box">
            <div class="feature-icon">⏱️</div>
            <h3 class="feature-title">7-Day Return Window</h3>
            <p style="color: var(--text-muted); font-size: 0.95rem;">Eligible orders can be returned within 7 calendar days after confirmed delivery with simple automated verification.</p>
        </div>
        <div class="feature-box">
            <div class="feature-icon">📸</div>
            <h3 class="feature-title">Evidence Upload</h3>
            <p style="color: var(--text-muted); font-size: 0.95rem;">Upload photographic proof of damages or discrepancies directly from your phone or PC for faster approval.</p>
        </div>
        <div class="feature-box">
            <div class="feature-icon">💳</div>
            <h3 class="feature-title">Refund or Replacement</h3>
            <p style="color: var(--text-muted); font-size: 0.95rem;">Choose between an instant balance refund or an exchange replacement with full status audit trails.</p>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
