<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Login" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="auth-wrapper">
    <div class="auth-card">
        <div class="auth-header">
            <a href="${pageContext.request.contextPath}/" class="brand-logo" style="justify-content: center; margin-bottom: 0.75rem;">
                <span>📦</span> ReturnHub
            </a>
            <h1 class="auth-title">Welcome Back</h1>
            <p class="auth-subtitle">Sign in to manage your orders and returns</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <form action="${pageContext.request.contextPath}/login" method="POST">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <div class="form-group">
                <label for="identifier" class="form-label">Username or Email</label>
                <input type="text" id="identifier" name="identifier" class="form-control"
                       value="<c:out value="${identifier}"/>" required autofocus placeholder="e.g. john_doe or john@example.com">
            </div>

            <div class="form-group">
                <label for="password" class="form-label">Password</label>
                <input type="password" id="password" name="password" class="form-control" required placeholder="••••••••">
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="padding: 0.75rem; margin-top: 0.5rem;">
                Sign In
            </button>
        </form>

        <div style="margin-top: 1.75rem; text-align: center; font-size: 0.9rem; color: var(--text-muted);">
            Don't have an account?
            <a href="${pageContext.request.contextPath}/register" style="font-weight: 600;">Create one here</a>
        </div>

        <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid var(--border); font-size: 0.8rem; color: var(--text-muted); text-align: center;">
            <strong>Demo Accounts:</strong><br>
            Customer: <code>john_doe</code> / <code>customer123</code><br>
            Customer: <code>jane_smith</code> / <code>customer123</code>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
