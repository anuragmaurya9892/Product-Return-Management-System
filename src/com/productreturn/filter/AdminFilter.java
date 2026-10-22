package com.productreturn.filter;

import com.productreturn.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter that restricts access to /admin/* endpoints exclusively to users with the ADMIN role.
 */
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=Admin+access+requires+login");
            return;
        }

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            // Logged in as customer, attempting to access admin
            res.sendRedirect(req.getContextPath() + "/customer/dashboard?error=Unauthorized+access+to+admin+area");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
