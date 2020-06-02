package com.miiguar.hfms.view.config;

import com.miiguar.hfms.utils.Constants;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author bernard
 */
@WebFilter(urlPatterns = "/*", description = "Front-end Session checker/filter.")
public class AppFilter implements Filter {
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        config.getServletContext().log("Session Checker.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // check token for sessions
        // allow url entry for login and registration
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        JwtTokenUtil tokenUtil = new JwtTokenUtil();

        String token = getTokenFromCookie(req);

        if (token.isEmpty()) {
            resp.sendRedirect("registration/login");
        } else {
            token = normalizeToken(token);

            if (tokenUtil.verifyToken(Constants.ISSUER, "login", token)) {
                resp.sendRedirect("dashboard");
            } else {
                resp.sendRedirect("registration/login");
            }
        }
    }

    private String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }

    @Override
    public void destroy() {

    }

    private String normalizeToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        return token.trim();
    }
}
