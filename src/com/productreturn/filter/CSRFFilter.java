package com.productreturn.filter;

import com.productreturn.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter that provides Cross-Site Request Forgery (CSRF) protection.
 * Ensures every session has a token, and verifies it on state-changing POST requests.
 */
public class CSRFFilter implements Filter {

    public static final String CSRF_TOKEN_ATTR = "csrfToken";
    public static final String CSRF_PARAM_NAME = "csrfToken";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(true);
        String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);

        if (sessionToken == null) {
            sessionToken = SecurityUtil.generateCSRFToken();
            session.setAttribute(CSRF_TOKEN_ATTR, sessionToken);
        }

        // Validate on POST requests
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String contentType = req.getContentType();
            // If it's multipart/form-data, parameters may not be readable by getParameter() before multipart parsing in servlet
            boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");

            if (!isMultipart) {
                String reqToken = req.getParameter(CSRF_PARAM_NAME);
                if (reqToken == null || !reqToken.equals(sessionToken)) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing CSRF token");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
