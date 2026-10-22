<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Error" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="content-wrapper">
    <div class="container" style="max-width: 600px; text-align: center; padding-top: 4rem;">
        <div class="card" style="padding: 3rem 2rem;">
            <div style="font-size: 4rem; margin-bottom: 1rem;">⚠️</div>
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.75rem;">
                <c:choose>
                    <c:when test="${param.code == '404'}">404 - Page Not Found</c:when>
                    <c:when test="${param.code == '500'}">500 - Server Error</c:when>
                    <c:otherwise>Something went wrong</c:otherwise>
                </c:choose>
            </h1>
            <p style="color: var(--text-muted); margin-bottom: 2rem;">
                <c:choose>
                    <c:when test="${param.code == '404'}">
                        The page or resource you are looking for does not exist or may have been moved.
                    </c:when>
                    <c:otherwise>
                        An unexpected issue occurred while processing your request. Please try again later.
                    </c:otherwise>
                </c:choose>
            </p>
            <div>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Return to Homepage</a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/includes/footer.jsp"/>
