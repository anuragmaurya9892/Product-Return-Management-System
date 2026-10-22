<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Submit Return Request" scope="request"/>
<c:set var="activePage" value="orders" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container" style="max-width: 800px;">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/customer/order?id=${order.id}" class="btn btn-outline btn-sm">← Back to Order</a>
        </div>

        <div class="card">
            <div class="card-header">
                <div>
                    <h1 class="card-title">Request Product Return</h1>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">
                        Order #<c:out value="${order.orderNumber}"/> — Delivered on <c:out value="${order.deliveredDate}"/>
                    </span>
                </div>
            </div>

            <jsp:include page="/includes/messages.jsp"/>

            <!-- Item Summary -->
            <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 1.25rem; margin-bottom: 1.5rem; display: flex; align-items: center; justify-content: space-between;">
                <div>
                    <h3 style="font-size: 1.15rem; color: var(--navy); margin-bottom: 0.25rem;"><c:out value="${item.product.name}"/></h3>
                    <div style="font-size: 0.85rem; color: var(--text-muted);">
                        SKU: <code><c:out value="${item.product.sku}"/></code> | Category: <c:out value="${item.product.category}"/>
                    </div>
                </div>
                <div style="text-align: right;">
                    <div style="font-size: 1.25rem; font-weight: 700; color: var(--navy);">₹<c:out value="${item.unitPrice}"/></div>
                    <div style="font-size: 0.8rem; color: var(--text-muted);">Purchased: <c:out value="${item.quantity}"/> (Eligible to return: <c:out value="${maxEligibleQty}"/>)</div>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/customer/return/new" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <input type="hidden" name="orderItemId" value="${item.id}">

                <div class="form-group">
                    <label for="quantity" class="form-label">Return Quantity *</label>
                    <input type="number" id="quantity" name="quantity" class="form-control"
                           min="1" max="${maxEligibleQty}" value="1" required style="max-width: 200px;">
                    <small class="form-help">Maximum eligible for return: ${maxEligibleQty}</small>
                </div>

                <div class="form-group">
                    <label for="reason" class="form-label">Return Reason *</label>
                    <select id="reason" name="reason" class="form-select" required>
                        <option value="" disabled selected>-- Select a valid reason --</option>
                        <option value="DAMAGED">Damaged Product (Item arrived broken or packaging damaged)</option>
                        <option value="DEFECTIVE">Defective Product (Item does not work properly or has flaws)</option>
                        <option value="WRONG_ITEM">Wrong Item Received (Item differs from what was ordered)</option>
                        <option value="INCORRECT_SIZE">Incorrect Size (Size did not fit as expected)</option>
                        <option value="NOT_NEEDED">No Longer Needed (Change of mind)</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Preferred Resolution *</label>
                    <div style="display: flex; gap: 2rem; margin-top: 0.5rem;">
                        <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                            <input type="radio" name="resolutionType" value="REFUND" checked>
                            <strong>Refund</strong> (Credit back purchase price to original payment method)
                        </label>
                        <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                            <input type="radio" name="resolutionType" value="REPLACEMENT">
                            <strong>Replacement</strong> (Ship an identical replacement item)
                        </label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="returnDescription" class="form-label">Description of Issue (Optional)</label>
                    <textarea id="returnDescription" name="description" class="form-control" rows="3" maxlength="1000"
                              placeholder="Please provide details about what is wrong with the item to expedite review..."></textarea>
                    <small class="form-help"><span id="charCount">0</span>/1000 characters</small>
                </div>

                <div class="form-group">
                    <label for="evidenceImage" class="form-label">Upload Evidence Image (Optional)</label>
                    <input type="file" id="evidenceImage" name="evidenceImage" class="form-control" accept=".jpg,.jpeg,.png">
                    <small class="form-help">Supported formats: JPG, JPEG, PNG. Max file size: 5 MB.</small>
                    <div id="imagePreview"></div>
                </div>

                <div style="display: flex; justify-content: flex-end; gap: 1rem; margin-top: 2rem; padding-top: 1rem; border-top: 1px solid var(--border);">
                    <a href="${pageContext.request.contextPath}/customer/order?id=${order.id}" class="btn btn-outline">Cancel</a>
                    <button type="submit" class="btn btn-primary" style="padding: 0.75rem 2rem;">
                        Submit Return Request
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
