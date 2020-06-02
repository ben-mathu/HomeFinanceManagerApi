package com.miiguar.hfms.config;

import com.google.gson.Gson;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.Constants;
import com.miiguar.hfms.utils.Log;
import com.miiguar.tokengeneration.JwtTokenUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author bernard
 */
@WebFilter(urlPatterns = "/api/*", description = "API Session Checker Filter.")
public class ApiFilters implements Filter {
    private static final String TAG = ApiFilters.class.getSimpleName();
    
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
        
        Log.d(TAG, url.toString());

        String endPoint = req.getRequestURI();
        if (!endPoint.endsWith("/login") && !endPoint.endsWith("/registration")) {
            if (req.getHeader("Authorization") == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter writer = resp.getWriter();

                Report report = new Report();
                report.setMessage("Requires token");
                report.setStatus(HttpServletResponse.SC_FORBIDDEN);

                Gson gson = new Gson();
                writer.write(gson.toJson(report));
            } else {
                String token = normalizeToken(req);
                if (jwtTokenUtil.verifyToken(Constants.ISSUER, "login", token)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    PrintWriter writer = resp.getWriter();

                    Report report = new Report();
                    report.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    report.setMessage("Invalid token");

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
