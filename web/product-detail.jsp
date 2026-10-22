<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="${product.name}" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <jsp:include page="/includes/customer-nav.jsp"/>
    </c:when>
    <c:otherwise>
        <nav class="navbar">
            <div class="container nav-container">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <span>📦</span> ReturnHub
                </a>
                <ul class="nav-menu">
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Login</a></li>
                </ul>
            </div>
        </nav>
    </c:otherwise>
</c:choose>

<main class="content-wrapper">
    <div class="container">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline btn-sm">← Back to Products</a>
        </div>

        <div class="card" style="padding: 2.5rem;">
            <div style="display: grid; grid-template-columns: 1fr 1.5fr; gap: 3rem;">
                <div style="background: #f1f5f9; border-radius: var(--radius-md); display: flex; align-items: center; justify-content: center; min-height: 320px; font-size: 5rem;">
                    📦
                </div>

                <div>
                    <span class="product-category-tag" style="font-size: 0.85rem;"><c:out value="${product.category}"/></span>
                    <h1 style="font-size: 2rem; color: var(--navy); margin: 0.5rem 0 1rem 0;"><c:out value="${product.name}"/></h1>

                    <div style="font-size: 2rem; font-weight: 800; color: var(--navy); margin-bottom: 1.5rem;">
                        ₹<c:out value="${product.price}"/>
                    </div>

                    <div style="margin-bottom: 1.5rem;">
                        <span class="badge" style="background:#ecfdf5; color:#065f46; font-size: 0.85rem;">
                            ✔ In Stock (<c:out value="${product.stockQuantity}"/> units available)
                        </span>
                        <span style="font-size: 0.85rem; color: var(--text-muted); margin-left: 1rem;">
                            SKU: <code><c:out value="${product.sku}"/></code>
                        </span>
                    </div>

                    <h3 style="font-size: 1.1rem; color: var(--navy); margin-bottom: 0.5rem;">Product Description</h3>
                    <p style="color: var(--text-main); line-height: 1.7; margin-bottom: 2rem;">
                        <c:out value="${product.description}"/>
                    </p>

                    <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 1.25rem;">
                        <h4 style="font-size: 0.95rem; color: var(--navy); margin-bottom: 0.35rem;">🛡️ Return Policy Guaranteed</h4>
                        <p style="font-size: 0.85rem; color: var(--text-muted);">
                            This item is covered under our <strong>7-day replacement/refund guarantee</strong> starting from delivery date.
                            If damaged, defective, or incorrect, request a return directly from your Order History.
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
