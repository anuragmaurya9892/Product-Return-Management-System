<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Process Return ${returnRequest.returnNumber}" scope="request"/>
<c:set var="activePage" value="returns" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content" style="max-width: 1000px;">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/admin/returns" class="btn btn-outline btn-sm">← Back to Returns List</a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <!-- Return Overview Card -->
        <div class="card">
            <div class="card-header">
                <div>
                    <h1 class="card-title">Return Ref: <c:out value="${returnRequest.returnNumber}"/></h1>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">
                        Submitted on <c:out value="${returnRequest.createdAt}"/> by
                        <strong><c:out value="${returnRequest.user.fullName}"/></strong> (<c:out value="${returnRequest.user.email}"/>)
                    </span>
                </div>
                <div>
                    <span class="badge badge-${returnRequest.status.toLowerCase().replace('_', '-')}" style="font-size: 0.95rem; padding: 0.5rem 1rem;">
                        <c:out value="${returnRequest.status.replace('_', ' ')}"/>
                    </span>
                </div>
            </div>

            <!-- Details Grid -->
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Product</div>
                    <div style="font-size: 1.1rem; font-weight: 700; color: var(--navy);"><c:out value="${returnRequest.orderItem.product.name}"/></div>
                    <div style="font-size: 0.8rem; color: var(--text-muted);">SKU: <c:out value="${returnRequest.orderItem.product.sku}"/></div>
                </div>

                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Quantity Returned</div>
                    <div style="font-size: 1.25rem; font-weight: 700; color: var(--navy);">
                        <c:out value="${returnRequest.quantity}"/> unit(s)
                    </div>
                </div>

                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Reported Reason</div>
                    <div style="font-size: 1rem; font-weight: 600; color: var(--navy);"><c:out value="${returnRequest.reason.replace('_', ' ')}"/></div>
                </div>

                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Requested Resolution</div>
                    <div style="font-size: 1.1rem; font-weight: 700; color: ${returnRequest.resolutionType == 'REFUND' ? 'var(--primary)' : 'var(--purple)'};">
                        <c:out value="${returnRequest.resolutionType}"/>
                    </div>
                </div>
            </div>

            <!-- Customer Notes -->
            <c:if test="${not empty returnRequest.description}">
                <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 1.25rem; margin-bottom: 1.5rem;">
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600; margin-bottom: 0.35rem;">Customer Notes</div>
                    <p style="color: var(--text-main);"><c:out value="${returnRequest.description}"/></p>
                </div>
            </c:if>

            <!-- Evidence Image -->
            <c:if test="${not empty returnRequest.evidenceImage}">
                <div style="margin-bottom: 1.5rem;">
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600; margin-bottom: 0.5rem;">Photographic Evidence Uploaded by Customer</div>
                    <a href="${pageContext.request.contextPath}/${returnRequest.evidenceImage}" target="_blank">
                        <img src="${pageContext.request.contextPath}/${returnRequest.evidenceImage}" alt="Customer Evidence"
                             style="max-width: 250px; max-height: 250px; border-radius: var(--radius-sm); border: 1px solid var(--border);">
                    </a>
                </div>
            </c:if>

            <!-- Refund Info -->
            <c:if test="${not empty refund}">
                <div style="background: var(--success-bg); border: 1px solid #a7f3d0; border-radius: var(--radius-sm); padding: 1.25rem; margin-bottom: 1.5rem;">
                    <div style="display: flex; justify-content: space-between; align-items: center;">
                        <div>
                            <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--success); font-weight: 700;">Refund Ledger Record</div>
                            <div style="font-size: 1.35rem; font-weight: 800; color: #065f46;">₹<c:out value="${refund.amount}"/></div>
                        </div>
                        <div>
                            <span class="badge badge-${refund.status == 'PROCESSED' ? 'refunded' : 'pending'}">
                                Refund <c:out value="${refund.status}"/>
                            </span>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Admin Processing Form -->
            <div style="background: #ffffff; border: 2px solid var(--primary); border-radius: var(--radius-md); padding: 1.5rem; margin-top: 1.5rem;">
                <h3 style="font-size: 1.15rem; color: var(--navy); margin-bottom: 0.5rem;">Update Return Status</h3>
                <p style="font-size: 0.85rem; color: var(--text-muted); margin-bottom: 1.25rem;">
                    Follow the valid workflow rules:
                    <code>Pending → Approved / Rejected → Item Received → ${returnRequest.resolutionType == 'REFUND' ? 'Refund Processed' : 'Replacement Shipped'}</code>
                </p>

                <c:choose>
                    <c:when test="${empty nextStatuses}">
                        <div class="alert alert-info" style="margin-bottom: 0;">
                            ℹ️ This return request is in a <strong>terminal state</strong> (${returnRequest.status.replace('_', ' ')}). No further status updates are permitted.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <form action="${pageContext.request.contextPath}/admin/return" method="POST">
                            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                            <input type="hidden" name="id" value="${returnRequest.id}">

                            <div class="form-group">
                                <label for="newStatus" class="form-label">Next Action / Status *</label>
                                <select id="newStatus" name="newStatus" class="form-select" required style="max-width: 350px;">
                                    <option value="" disabled selected>-- Select Next Status --</option>
                                    <c:forEach var="st" items="${nextStatuses}">
                                        <option value="${st}"><c:out value="${st.replace('_', ' ')}"/></option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="adminNote" class="form-label">Administrator Note / Reason</label>
                                <textarea id="adminNote" name="adminNote" class="form-control" rows="2"
                                          placeholder="Enter internal inspection notes, reason for decision, or tracking details..."></textarea>
                            </div>

                            <button type="submit" class="btn btn-primary" data-confirm="Confirm changing status of return ${returnRequest.returnNumber}?">
                                Apply Status Update
                            </button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Status Timeline -->
            <div style="margin-top: 2.5rem;">
                <h3 style="font-size: 1.15rem; color: var(--navy); margin-bottom: 1rem;">Full Audit Trail</h3>
                <div class="timeline">
                    <c:forEach var="h" items="${returnRequest.statusHistory}">
                        <div class="timeline-item">
                            <div class="timeline-point ${h.newStatus == returnRequest.status ? 'active' : ''}"></div>
                            <div class="timeline-content">
                                <div class="timeline-header">
                                    <span class="timeline-title">
                                        <c:choose>
                                            <c:when test="${empty h.oldStatus}">
                                                Initial Submission (${h.newStatus.replace('_', ' ')})
                                            </c:when>
                                            <c:otherwise>
                                                Transition: ${h.oldStatus.replace('_', ' ')} → ${h.newStatus.replace('_', ' ')}
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="timeline-time"><c:out value="${h.changedAt}"/></span>
                                </div>
                                <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.25rem;">
                                    Action by: <strong><c:out value="${h.changedBy}"/></strong>
                                </div>
                                <c:if test="${not empty h.note}">
                                    <div class="timeline-note"><c:out value="${h.note}"/></div>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
