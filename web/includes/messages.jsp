<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<c:if test="${not empty param.error}">
    <div class="alert alert-error">
        <span>⚠️ <c:out value="${param.error}"/></span>
    </div>
</c:if>

<c:if test="${not empty param.success}">
    <div class="alert alert-success">
        <span>✅ <c:out value="${param.success}"/></span>
    </div>
</c:if>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-error">
        <span>⚠️ <c:out value="${errorMessage}"/></span>
    </div>
</c:if>

<c:if test="${not empty successMessage}">
    <div class="alert alert-success">
        <span>✅ <c:out value="${successMessage}"/></span>
    </div>
</c:if>
