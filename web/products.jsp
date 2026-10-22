<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Product Catalogue" scope="request"/>
<c:set var="activePage" value="products" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<c:choose>
    <c:when test="${sessionScope.user.role == 'ADMIN'}">
        <jsp:include page="/includes/customer-nav.jsp"/>
    </c:when>
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
                    <li><a href="${pageContext.request.contextPath}/products" class="nav-link active">Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="btn btn-outline btn-sm">Login</a></li>
                    <li><a href="${pageContext.request.contextPath}/register" class="btn btn-primary btn-sm">Register</a></li>
                </ul>
            </div>
        </nav>
    </c:otherwise>
</c:choose>

<main class="content-wrapper">
    <div class="container">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.5rem;">Explore Products</h1>
            <p style="color: var(--text-muted);">Browse our full inventory. All purchases qualify for our 7-day return policy.</p>
        </div>

        <form action="${pageContext.request.contextPath}/products" method="GET" class="filter-bar">
            <div class="filter-item" style="flex: 2 1 280px;">
                <label for="search" class="form-label">Search</label>
                <input type="text" id="search" name="search" class="form-control"
                       placeholder="Search products by name, SKU, or keywords..."
                       value="<c:out value="${searchQuery}"/>">
            </div>

            <div class="filter-item">
                <label for="category" class="form-label">Category</label>
                <select id="category" name="category" class="form-select" onchange="this.form.submit()">
                    <option value="ALL">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat}" ${selectedCategory == cat ? 'selected' : ''}>
                            <c:out value="${cat}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="filter-item" style="flex: 0 0 auto;">
                <button type="submit" class="btn btn-primary">Search</button>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Reset</a>
            </div>
        </form>

        <c:choose>
            <c:when test="${empty products}">
                <div class="card empty-state">
                    <div class="empty-state-icon">🔍</div>
                    <h3 class="empty-state-title">No products found</h3>
                    <p>Try modifying your search query or removing the category filter.</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="products-grid">
                    <c:forEach var="p" items="${products}">
                        <div class="product-card">
                            <div class="product-image-box">
                                <span>📦</span>
                            </div>
                            <div class="product-card-body">
                                <span class="product-category-tag"><c:out value="${p.category}"/></span>
                                <h3 class="product-title"><c:out value="${p.name}"/></h3>
                                <p class="product-description-clamp"><c:out value="${p.description}"/></p>

                                <div class="product-price-row">
                                    <div>
                                        <div class="product-price">₹<c:out value="${p.price}"/></div>
                                        <div style="font-size: 0.8rem; color: var(--text-muted);">SKU: <c:out value="${p.sku}"/></div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="btn btn-outline btn-sm">
                                        View Details
                                    </a>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
