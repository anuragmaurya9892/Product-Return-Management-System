package com.productreturn.filter;

import com.productreturn.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Restricts {@code /customer/*} to CUSTOMER accounts. Administrators are sent to the
 * admin console so they cannot accidentally shop or file returns as staff.
 * (Unauthenticated users are already handled by {@link AuthFilter}.)
 */
public class CustomerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = session != null ? (User) session.getAttribute("user") : null;

        if (user != null && "ADMIN".equalsIgnoreCase(user.getRole())) {
            res.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        }
        chain.doFilter(request, response);
    }
}
