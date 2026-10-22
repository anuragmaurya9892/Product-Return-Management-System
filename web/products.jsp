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
        <div style="display: flex; justify-content: space-between; align-items: flex-end; flex-wrap: wrap; gap: 1rem; margin-bottom: 1.5rem;">
            <div>
                <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Shop Products</h1>
                <p style="color: var(--text-muted);">Free express shipping on orders over ₹999 • Covered by our 7-day hassle-free return guarantee.</p>
            </div>
            <c:if test="${not empty sessionScope.user and sessionScope.user.role != 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/customer/cart" class="btn btn-outline">
                    🛒 View Cart (<c:out value="${sessionScope.cartCount != null ? sessionScope.cartCount : 0}"/>)
                </a>
            </c:if>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

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
                            <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="product-image-box" style="text-decoration: none; position: relative;">
                                <span>📦</span>
                                <span style="position: absolute; top: 10px; right: 10px;">
                                    <c:choose>
                                        <c:when test="${p.stockQuantity <= 0}">
                                            <span class="stock-pill out-of-stock">Out of Stock</span>
                                        </c:when>
                                        <c:when test="${p.stockQuantity <= 5}">
                                            <span class="stock-pill low-stock">⚡ Only <c:out value="${p.stockQuantity}"/> left</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock-pill in-stock">✔ In Stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </a>
                            <div class="product-card-body">
                                <span class="product-category-tag"><c:out value="${p.category}"/></span>
                                <h3 class="product-title">
                                    <a href="${pageContext.request.contextPath}/product?id=${p.id}" style="color: var(--navy); text-decoration: none;">
                                        <c:out value="${p.name}"/>
                                    </a>
                                </h3>
                                <p class="product-description-clamp"><c:out value="${p.description}"/></p>

                                <div class="product-price-row">
                                    <div>
                                        <div class="product-price">₹<c:out value="${p.price}"/></div>
                                        <div style="font-size: 0.78rem; color: var(--text-muted);">SKU: <c:out value="${p.sku}"/></div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="btn btn-outline btn-sm">
                                        Details
                                    </a>
                                </div>

                                <c:choose>
                                    <c:when test="${p.stockQuantity <= 0}">
                                        <div class="product-card-actions">
                                            <button class="btn btn-outline btn-sm btn-block" disabled style="opacity: 0.55; cursor: not-allowed;">
                                                Currently Unavailable
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:when test="${empty sessionScope.user}">
                                        <div class="product-card-actions">
                                            <a href="${pageContext.request.contextPath}/login?error=Please+log+in+to+order+products" class="btn btn-primary btn-sm btn-block">
                                                🛒 Sign in to Order
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:when test="${sessionScope.user.role == 'ADMIN'}">
                                        <div class="product-card-actions">
                                            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline btn-sm btn-block">
                                                Manage in Admin
                                            </a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="product-card-actions">
                                            <form action="${pageContext.request.contextPath}/customer/cart" method="POST" style="flex: 1;">
                                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="action" value="add">
                                                <input type="hidden" name="productId" value="${p.id}">
                                                <input type="hidden" name="quantity" value="1">
                                                <button type="submit" class="btn btn-outline btn-sm btn-block">
                                                    + Add to Cart
                                                </button>
                                            </form>
                                            <form action="${pageContext.request.contextPath}/customer/cart" method="POST" style="flex: 1;">
                                                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                                <input type="hidden" name="action" value="add">
                                                <input type="hidden" name="productId" value="${p.id}">
                                                <input type="hidden" name="quantity" value="1">
                                                <input type="hidden" name="buyNow" value="1">
                                                <button type="submit" class="btn btn-primary btn-sm btn-block">
                                                    ⚡ Buy Now
                                                </button>
                                            </form>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
