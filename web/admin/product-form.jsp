<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="${not empty product ? 'Edit Product' : 'Add Product'}" scope="request"/>
<c:set var="activePage" value="products" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content" style="max-width: 800px;">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline btn-sm">← Back to Products</a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <div class="card-header">
                <h1 class="card-title">
                    <c:choose>
                        <c:when test="${not empty product}">Edit Product #<c:out value="${product.id}"/></c:when>
                        <c:otherwise>Create New Product</c:otherwise>
                    </c:choose>
                </h1>
            </div>

            <form action="${pageContext.request.contextPath}/admin/products" method="POST">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <c:if test="${not empty product}">
                    <input type="hidden" name="id" value="${product.id}">
                </c:if>

                <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label for="name" class="form-label">Product Name *</label>
                        <input type="text" id="name" name="name" class="form-control"
                               value="<c:out value="${product.name}"/>" required placeholder="e.g. Wireless Ergonomic Mouse">
                    </div>

                    <div class="form-group">
                        <label for="sku" class="form-label">SKU *</label>
                        <input type="text" id="sku" name="sku" class="form-control"
                               value="<c:out value="${product.sku}"/>" required placeholder="e.g. ELEC-WEM-001">
                    </div>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label for="category" class="form-label">Category *</label>
                        <input type="text" id="category" name="category" class="form-control"
                               value="<c:out value="${product.category}"/>" required placeholder="e.g. Electronics">
                    </div>

                    <div class="form-group">
                        <label for="price" class="form-label">Price (INR) *</label>
                        <input type="number" step="0.01" min="0" id="price" name="price" class="form-control"
                               value="<c:out value="${product.price}"/>" required placeholder="1499.00">
                    </div>

                    <div class="form-group">
                        <label for="stockQuantity" class="form-label">Stock Quantity *</label>
                        <input type="number" min="0" id="stockQuantity" name="stockQuantity" class="form-control"
                               value="<c:out value="${product != null ? product.stockQuantity : 50}"/>" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description" class="form-label">Description</label>
                    <textarea id="description" name="description" class="form-control" rows="3"
                              placeholder="Full product overview and specifications..."><c:out value="${product.description}"/></textarea>
                </div>

                <div class="form-group">
                    <label for="imageUrl" class="form-label">Image URL / Relative Path</label>
                    <input type="text" id="imageUrl" name="imageUrl" class="form-control"
                           value="<c:out value="${product.imageUrl}"/>" placeholder="/images/products/mouse.jpg">
                </div>

                <div class="form-group">
                    <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                        <input type="checkbox" name="active" ${product == null || product.active ? 'checked' : ''}>
                        <strong>Active Product</strong> (Visible in customer catalogue)
                    </label>
                </div>

                <div style="margin-top: 2rem; padding-top: 1rem; border-top: 1px solid var(--border); display: flex; justify-content: flex-end; gap: 1rem;">
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-outline">Cancel</a>
                    <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem;">
                        Save Product
                    </button>
                </div>
            </form>
        </div>
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
