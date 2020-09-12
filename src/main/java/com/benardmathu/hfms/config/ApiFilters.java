package com.benardmathu.hfms.config;

import com.google.gson.Gson;
import com.benardmathu.hfms.data.status.Report;
import com.benardmathu.hfms.utils.Log;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import static com.benardmathu.hfms.utils.Constants.*;
import com.benardmathu.tokengeneration.JwtTokenUtil;

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

        String uri = req.getRequestURI();
        
        Log.d(TAG, uri);

        String endPoint = req.getRequestURI();
        if (!endPoint.endsWith("/login-user") && !endPoint.endsWith("/registration")) {
            if (req.getHeader("Authorization") == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                PrintWriter writer = resp.getWriter();

                Report report = new Report();
                report.setMessage("Requires token");
                report.setStatus(HttpServletResponse.SC_FORBIDDEN);

                Gson gson = new Gson();
                writer.write(gson.toJson(report));
            } else {
                // get Subject
                ConfigureApp app = new ConfigureApp();
                Properties prop = app.getProperties();
                String subject = prop.getProperty(SUBJECT, "");
                String token = null;
                try {
                    token = normalizeToken(req);
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "Error normalizing token.", e);
                }

                if (jwtTokenUtil.verifyToken(ISSUER, subject, token)) {
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

    private String normalizeToken(HttpServletRequest req) throws IllegalAccessException {
        String token = req.getHeader("Authorization");
        if (token.startsWith("Bearer ")) {
            token = token.split(" ")[1];
        } else if(token.isEmpty()) {
            throw new IllegalArgumentException("Token is null");
        } else {
            throw new IllegalAccessException("Token structure invalid.");
        }

        return token.trim();
    }

    @Override
    public void destroy() {
        config.getServletContext().log("Destroying session checker filter.");
    }
}
