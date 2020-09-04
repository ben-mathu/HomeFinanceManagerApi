package com.miiguar.hfms.view.config;

import com.miiguar.hfms.config.ConfigureApp;
import com.miiguar.hfms.utils.Log;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static com.miiguar.hfms.utils.Constants.*;

/**
 * @author bernard
 */
@WebFilter(urlPatterns = "/login", description = "Front-end Session checker/filter.")
public class LoginTokenFilter implements Filter {
    private static final String TAG = LoginTokenFilter.class.getSimpleName();
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
        String endpoint = req.getRequestURI();
        Log.d(TAG, endpoint);

        if (!token.isEmpty()) {
            token = normalizeToken(token);
            ConfigureApp app = new ConfigureApp();
            Properties prop = app.getProperties();
            String subject = prop.getProperty(SUBJECT, "");

            if (tokenUtil.verifyToken(ISSUER, subject, token)) {
                resp.sendRedirect("dashboard");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String getTokenFromCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN.equals(cookie.getName())) {
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
