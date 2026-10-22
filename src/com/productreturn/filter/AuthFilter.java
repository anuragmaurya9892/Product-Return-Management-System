package com.productreturn.filter;

import com.productreturn.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter that enforces authentication for protected resources.
 * Allows public access to landing page, products, login, register, and static assets.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();

        // Allow static resources and public paths
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Save requested URL for redirect after login if needed
            res.sendRedirect(req.getContextPath() + "/login?error=Please+log+in+to+continue");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        if (path == null || path.isEmpty() || "/".equals(path) || "/index.jsp".equals(path)) {
            return true;
        }
        if (path.startsWith("/login") || path.startsWith("/register") ||
            path.startsWith("/products") || path.startsWith("/product") ||
            path.startsWith("/css/") || path.startsWith("/js/") ||
            path.startsWith("/images/") || path.startsWith("/uploads/") ||
            path.startsWith("/error.jsp")) {
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}
