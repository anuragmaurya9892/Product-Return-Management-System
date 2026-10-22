<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="My Profile" scope="request"/>
<c:set var="activePage" value="profile" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container" style="max-width: 650px;">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Customer Profile</h1>
            <p style="color: var(--text-muted);">Manage your personal details and primary shipping address.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <form action="${pageContext.request.contextPath}/customer/profile" method="POST">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">

                <div class="form-group">
                    <label for="username" class="form-label">Username (Read-Only)</label>
                    <input type="text" id="username" class="form-control" value="<c:out value="${userProfile.username}"/>" disabled style="background:#f1f5f9;">
                </div>

                <div class="form-group">
                    <label for="email" class="form-label">Email Address (Read-Only)</label>
                    <input type="email" id="email" class="form-control" value="<c:out value="${userProfile.email}"/>" disabled style="background:#f1f5f9;">
                </div>

                <div class="form-group">
                    <label for="fullName" class="form-label">Full Name *</label>
                    <input type="text" id="fullName" name="fullName" class="form-control"
                           value="<c:out value="${userProfile.fullName}"/>" required>
                </div>

                <div class="form-group">
                    <label for="phone" class="form-label">Phone Number</label>
                    <input type="tel" id="phone" name="phone" class="form-control"
                           value="<c:out value="${userProfile.phone}"/>">
                </div>

                <div class="form-group">
                    <label for="address" class="form-label">Default Shipping Address</label>
                    <textarea id="address" name="address" class="form-control" rows="3"><c:out value="${userProfile.address}"/></textarea>
                </div>

                <div style="margin-top: 2rem; padding-top: 1rem; border-top: 1px solid var(--border);">
                    <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem;">
                        Save Profile Changes
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
