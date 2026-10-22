<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Return ${returnRequest.returnNumber}" scope="request"/>
<c:set var="activePage" value="returns" scope="request"/>
<jsp:include page="/includes/header.jsp"/>
<jsp:include page="/includes/customer-nav.jsp"/>

<main class="content-wrapper">
    <div class="container" style="max-width: 900px;">
        <div style="margin-bottom: 1.5rem;">
            <a href="${pageContext.request.contextPath}/customer/returns" class="btn btn-outline btn-sm">← Back to My Returns</a>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <div class="card-header">
                <div>
                    <h1 class="card-title">Return Ref: <c:out value="${returnRequest.returnNumber}"/></h1>
                    <span style="font-size: 0.85rem; color: var(--text-muted);">
                        Submitted on <c:out value="${returnRequest.createdAt}"/>
                    </span>
                </div>
                <div>
                    <span class="badge badge-${returnRequest.status.toLowerCase().replace('_', '-')}" style="font-size: 0.9rem; padding: 0.4rem 0.8rem;">
                        <c:out value="${returnRequest.status.replace('_', ' ')}"/>
                    </span>
                </div>
            </div>

            <!-- Details Grid -->
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 1.5rem; margin-bottom: 2rem;">
                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Product</div>
                    <div style="font-size: 1.1rem; font-weight: 700; color: var(--navy);"><c:out value="${returnRequest.orderItem.product.name}"/></div>
                    <div style="font-size: 0.8rem; color: var(--text-muted);">SKU: <c:out value="${returnRequest.orderItem.product.sku}"/></div>
                </div>

                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Quantity Returned</div>
                    <div style="font-size: 1.25rem; font-weight: 700; color: var(--navy);"><c:out value="${returnRequest.quantity}"/> unit(s)</div>
                </div>

                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Reason</div>
                    <div style="font-size: 1rem; font-weight: 600; color: var(--navy);"><c:out value="${returnRequest.reason.replace('_', ' ')}"/></div>
                </div>

                <div>
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600;">Requested Resolution</div>
                    <div style="font-size: 1.1rem; font-weight: 700; color: ${returnRequest.resolutionType == 'REFUND' ? 'var(--primary)' : 'var(--purple)'};">
                        <c:out value="${returnRequest.resolutionType}"/>
                    </div>
                </div>
            </div>

            <!-- Description & Evidence Image -->
            <c:if test="${not empty returnRequest.description}">
                <div style="background: #f8fafc; border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 1.25rem; margin-bottom: 1.5rem;">
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600; margin-bottom: 0.35rem;">Customer Notes</div>
                    <p style="color: var(--text-main);"><c:out value="${returnRequest.description}"/></p>
                </div>
            </c:if>

            <c:if test="${not empty returnRequest.evidenceImage}">
                <div style="margin-bottom: 1.5rem;">
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--text-muted); font-weight: 600; margin-bottom: 0.5rem;">Uploaded Photographic Evidence</div>
                    <a href="${pageContext.request.contextPath}/${returnRequest.evidenceImage}" target="_blank">
                        <img src="${pageContext.request.contextPath}/${returnRequest.evidenceImage}" alt="Return Evidence"
                             style="max-width: 250px; max-height: 250px; border-radius: var(--radius-sm); border: 1px solid var(--border);">
                    </a>
                </div>
            </c:if>

            <c:if test="${not empty returnRequest.adminNote}">
                <div style="background: #fffbeb; border: 1px solid #fde68a; border-radius: var(--radius-sm); padding: 1.25rem; margin-bottom: 1.5rem;">
                    <div style="font-size: 0.8rem; text-transform: uppercase; color: var(--warning); font-weight: 700; margin-bottom: 0.35rem;">Note from ReturnHub Administrator</div>
                    <p style="color: #92400e; font-weight: 500;"><c:out value="${returnRequest.adminNote}"/></p>
                </div>
            </c:if>

            <!-- Refund Info (if resolution is REFUND) -->
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
                            <c:if test="${not empty refund.processedAt}">
                                <div style="font-size: 0.75rem; color: #065f46; margin-top: 3px;">
                                    Processed on: <c:out value="${refund.processedAt}"/>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Status Timeline -->
            <div style="margin-top: 2rem;">
                <h3 style="font-size: 1.15rem; color: var(--navy); margin-bottom: 1rem;">Status Audit History</h3>
                <div class="timeline">
                    <c:forEach var="h" items="${returnRequest.statusHistory}">
                        <div class="timeline-item">
                            <div class="timeline-point ${h.newStatus == returnRequest.status ? 'active' : ''}"></div>
                            <div class="timeline-content">
                                <div class="timeline-header">
                                    <span class="timeline-title">
                                        <c:choose>
                                            <c:when test="${empty h.oldStatus}">
                                                Request Submitted (${h.newStatus.replace('_', ' ')})
                                            </c:when>
                                            <c:otherwise>
                                                Status Changed: ${h.oldStatus.replace('_', ' ')} → ${h.newStatus.replace('_', ' ')}
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="timeline-time"><c:out value="${h.changedAt}"/></span>
                                </div>
                                <div style="font-size: 0.8rem; color: var(--text-muted); margin-bottom: 0.25rem;">
                                    By: <strong><c:out value="${h.changedBy}"/></strong>
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
    </div>
</main>

<jsp:include page="/includes/footer.jsp"/>
