package com.miiguar.hfms.utils;

import com.google.gson.Gson;
import com.miiguar.hfms.data.status.MessageReport;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author bernard
 */
public class AppFilters implements Filter {
    private FilterConfig config = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        config.getServletContext().log("Session Checker.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        // verify authorization token
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

        StringBuffer url = req.getRequestURL();
        System.out.println(url.toString());
        StringBuffer endPoint = req.getRequestURL();
        if (!req.getRequestURI().endsWith("/validate-user")
                && !req.getRequestURI().endsWith("/")
                && !req.getRequestURI().contains("static")
        ) {
            if (req.getHeader("Authorization") == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter writer = resp.getWriter();

                MessageReport report = new MessageReport(HttpServletResponse.SC_FORBIDDEN, "Requires token");

                Gson gson = new Gson();
                writer.write(gson.toJson(report));
            } else {
                String token = normalizeToken(req);
                if (jwtTokenUtil.verifyToken(Constants.ISSUER, "login", token)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    PrintWriter writer = resp.getWriter();

                    MessageReport report = new MessageReport(HttpServletResponse.SC_FORBIDDEN, "Invalid token");

                    Gson gson = new Gson();
                    writer.write(gson.toJson(report));
                }
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String normalizeToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");
        if (token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
        }

        return token.trim();
    }

    @Override
    public void destroy() {
        config.getServletContext().log("Destroying session checker filter.");
    }
}
