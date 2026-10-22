<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<c:set var="pageTitle" value="Customers" scope="request"/>
<c:set var="activePage" value="customers" scope="request"/>
<jsp:include page="/includes/header.jsp"/>

<div class="admin-layout">
    <jsp:include page="/includes/admin-nav.jsp"/>

    <main class="admin-content">
        <div style="margin-bottom: 2rem;">
            <h1 style="font-size: 2rem; color: var(--navy); margin-bottom: 0.35rem;">Customer Accounts</h1>
            <p style="color: var(--text-muted);">Registered customer directory with order and return aggregates.</p>
        </div>

        <jsp:include page="/includes/messages.jsp"/>

        <div class="card">
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Customer Name</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Orders</th>
                            <th>Returns</th>
                            <th>Joined On</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="c" items="${customers}">
                            <tr>
                                <td>#<c:out value="${c.id}"/></td>
                                <td><strong><c:out value="${c.fullName}"/></strong></td>
                                <td><code><c:out value="${c.username}"/></code></td>
                                <td><c:out value="${c.email}"/></td>
                                <td><c:out value="${not empty c.phone ? c.phone : 'N/A'}"/></td>
                                <td><span class="badge badge-placed"><c:out value="${orderCountMap[c.id]}"/> orders</span></td>
                                <td><span class="badge badge-pending"><c:out value="${returnCountMap[c.id]}"/> returns</span></td>
                                <td><c:out value="${c.createdAt}"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<jsp:include page="/includes/footer.jsp"/>
