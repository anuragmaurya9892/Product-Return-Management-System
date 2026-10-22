<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Register" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="auth-wrapper">
    <div class="auth-card wide">
        <div class="auth-header">
            <a href="${pageContext.request.contextPath}/" class="brand-logo" style="justify-content: center; margin-bottom: 0.75rem;">
                <span>📦</span> ReturnHub
            </a>
            <h1 class="auth-title">Create an Account</h1>
            <p class="auth-subtitle">Join ReturnHub to manage orders and request quick returns</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <form action="${pageContext.request.contextPath}/register" method="POST">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="fullName" class="form-label">Full Name *</label>
                    <input type="text" id="fullName" name="fullName" class="form-control"
                           value="<c:out value="${fullName}"/>" required placeholder="John Doe">
                </div>

                <div class="form-group">
                    <label for="username" class="form-label">Username *</label>
                    <input type="text" id="username" name="username" class="form-control"
                           value="<c:out value="${username}"/>" required placeholder="johndoe123">
                    <small class="form-help">3-50 alphanumeric characters or underscores</small>
                </div>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="email" class="form-label">Email Address *</label>
                    <input type="email" id="email" name="email" class="form-control"
                           value="<c:out value="${email}"/>" required placeholder="john@example.com">
                </div>

                <div class="form-group">
                    <label for="phone" class="form-label">Phone Number</label>
                    <input type="tel" id="phone" name="phone" class="form-control"
                           value="<c:out value="${phone}"/>" placeholder="+91 9876543210">
                </div>
            </div>

            <div class="form-group">
                <label for="address" class="form-label">Shipping / Home Address</label>
                <textarea id="address" name="address" class="form-control" rows="2"
                          placeholder="Street, City, State, PIN Code"><c:out value="${address}"/></textarea>
            </div>

            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                <div class="form-group">
                    <label for="password" class="form-label">Password *</label>
                    <input type="password" id="password" name="password" class="form-control" required placeholder="Min 8 chars, 1 letter, 1 number">
                </div>

                <div class="form-group">
                    <label for="confirmPassword" class="form-label">Confirm Password *</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" class="form-control" required placeholder="Re-type password">
                </div>
            </div>

            <button type="submit" class="btn btn-primary btn-block" style="padding: 0.75rem; margin-top: 0.5rem;">
                Complete Registration
            </button>
        </form>

        <div style="margin-top: 1.5rem; text-align: center; font-size: 0.9rem; color: var(--text-muted);">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login" style="font-weight: 600;">Sign in here</a>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
