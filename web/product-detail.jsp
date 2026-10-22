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

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card" style="padding: 2.5rem;">
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 3rem;">
                <div style="background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%); border-radius: var(--radius-md); display: flex; flex-direction: column; align-items: center; justify-content: center; min-height: 340px; font-size: 5.5rem; position: relative;">
                    📦
                    <div style="font-size: 0.85rem; color: var(--text-muted); margin-top: 1rem; font-weight: 600;">
                        SKU: <code><c:out value="${product.sku}"/></code>
                    </div>
                </div>

                <div>
                    <span class="product-category-tag" style="font-size: 0.85rem;"><c:out value="${product.category}"/></span>
                    <h1 style="font-size: 2rem; color: var(--navy); margin: 0.4rem 0 0.75rem 0;"><c:out value="${product.name}"/></h1>

                    <div style="display: flex; align-items: baseline; gap: 1rem; margin-bottom: 1.25rem;">
                        <div style="font-size: 2.15rem; font-weight: 800; color: var(--navy);">
                            ₹<c:out value="${product.price}"/>
                        </div>
                        <span style="font-size: 0.85rem; color: var(--success); font-weight: 600;">
                            Inclusive of all taxes
                        </span>
                    </div>

                    <div style="margin-bottom: 1.5rem;">
                        <c:choose>
                            <c:when test="${product.stockQuantity <= 0}">
                                <span class="stock-pill out-of-stock" style="font-size: 0.85rem; padding: 0.35rem 0.8rem;">
                                    ✖ Out of Stock
                                </span>
                            </c:when>
                            <c:when test="${product.stockQuantity <= 5}">
                                <span class="stock-pill low-stock" style="font-size: 0.85rem; padding: 0.35rem 0.8rem;">
                                    ⚡ Hurry! Only <c:out value="${product.stockQuantity}"/> left in stock
                                </span>
                            </c:when>
                            <c:otherwise>
                                <span class="stock-pill in-stock" style="font-size: 0.85rem; padding: 0.35rem 0.8rem;">
                                    ✔ In Stock (<c:out value="${product.stockQuantity}"/> units ready to ship)
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <h3 style="font-size: 1.05rem; color: var(--navy); margin-bottom: 0.4rem;">About this Item</h3>
                    <p style="color: var(--text-main); line-height: 1.7; margin-bottom: 1.75rem;">
                        <c:out value="${product.description}"/>
                    </p>

                    <!-- Purchase Action Box -->
                    <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-md); padding: 1.5rem; margin-bottom: 1.5rem;">
                        <c:choose>
                            <c:when test="${product.stockQuantity <= 0}">
                                <button class="btn btn-outline btn-block" disabled style="opacity: 0.6; cursor: not-allowed;">
                                    Out of Stock — Check Back Soon
                                </button>
                            </c:when>
                            <c:when test="${empty sessionScope.user}">
                                <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem;">
                                    <div>
                                        <div style="font-weight: 700; color: var(--navy);">Ready to order this product?</div>
                                        <div style="font-size: 0.85rem; color: var(--text-muted);">Sign in to your customer account to add items to your cart or buy immediately.</div>
                                    </div>
                                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">
                                        Sign In to Buy →
                                    </a>
                                </div>
                            </c:when>
                            <c:when test="${sessionScope.user.role == 'ADMIN'}">
                                <div style="display: flex; align-items: center; justify-content: space-between;">
                                    <span style="font-size: 0.9rem; color: var(--text-muted);">Viewing as Administrator</span>
                                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline btn-sm">Edit Inventory</a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <form action="${pageContext.request.contextPath}/customer/cart" method="POST">
                                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                                    <input type="hidden" name="action" value="add">
                                    <input type="hidden" name="productId" value="${product.id}">

                                    <div style="display: flex; align-items: center; gap: 1.25rem; flex-wrap: wrap; margin-bottom: 1.1rem;">
                                        <label class="form-label" style="margin-bottom: 0;">Quantity:</label>
                                        <div class="qty-stepper">
                                            <button type="button" class="qty-btn" data-qty-minus aria-label="Decrease quantity">−</button>
                                            <input type="number" name="quantity" class="qty-input" value="1"
                                                   min="1" max="${product.stockQuantity < 10 ? product.stockQuantity : 10}" readonly>
                                            <button type="button" class="qty-btn" data-qty-plus aria-label="Increase quantity">+</button>
                                        </div>
                                        <span style="font-size: 0.8rem; color: var(--text-muted);">
                                            Max <c:out value="${product.stockQuantity < 10 ? product.stockQuantity : 10}"/> per order
                                        </span>
                                    </div>

                                    <div style="display: flex; gap: 0.85rem; flex-wrap: wrap;">
                                        <button type="submit" name="buyNow" value="0" class="btn btn-outline" style="flex: 1; min-width: 160px; border-color: var(--primary); color: var(--primary);">
                                            🛒 Add to Cart
                                        </button>
                                        <button type="submit" name="buyNow" value="1" class="btn btn-primary" style="flex: 1; min-width: 160px;">
                                            ⚡ Buy Now (Instant Checkout)
                                        </button>
                                    </div>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <!-- Trust & Policy Perks -->
                    <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 0.85rem;">
                        <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 0.9rem;">
                            <div style="font-weight: 700; font-size: 0.9rem; color: var(--navy);">🚚 Free Shipping ₹999+</div>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">Fast doorstep delivery in 2–3 business days.</div>
                        </div>
                        <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 0.9rem;">
                            <div style="font-weight: 700; font-size: 0.9rem; color: var(--navy);">🛡️ 7-Day Easy Returns</div>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">Full refund or instant replacement after delivery.</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
