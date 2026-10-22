<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Manage Products" scope="request"/>
<c:set var="activePage" value="products" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem;">
            <div>
                <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Product Inventory</h1>
                <p style="color: var(--text-muted);">Manage catalogue items, stock levels, and active status.</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/products?action=new" class="btn btn-primary">
                + Add New Product
            </a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Product Name</th>
                            <th>SKU</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>#<c:out value="${p.id}"/></td>
                                <td><strong><c:out value="${p.name}"/></strong></td>
                                <td><code><c:out value="${p.sku}"/></code></td>
                                <td><c:out value="${p.category}"/></td>
                                <td><strong>₹<c:out value="${p.price}"/></strong></td>
                                <td><c:out value="${p.stockQuantity}"/> units</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.active}">
                                            <span class="badge badge-approved">Active</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-rejected">Inactive</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div style="display: flex; gap: 0.5rem;">
                                        <a href="${pageContext.request.contextPath}/admin/products?action=edit&id=${p.id}" class="btn btn-outline btn-sm">
                                            Edit
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/products?action=toggle&id=${p.id}"
                                           class="btn btn-sm ${p.active ? 'btn-danger' : 'btn-success'}"
                                           data-confirm="Toggle product status?">
                                            ${p.active ? 'Deactivate' : 'Activate'}
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
