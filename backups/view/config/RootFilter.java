package com.benardmathu.hfms.view.config;

import com.benardmathu.hfms.config.ConfigureApp;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static com.benardmathu.hfms.utils.Constants.ISSUER;
import static com.benardmathu.hfms.utils.Constants.TOKEN;
import com.benardmathu.tokengeneration.JwtTokenUtil;

/**
 * @author bernard
 */
public class RootFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext().log("Session Checker.");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getParameter(TOKEN);

        if (token.isEmpty()) filterChain.doFilter(request, response);
        JwtTokenUtil tokenUtil = new JwtTokenUtil();
        ConfigureApp app = new ConfigureApp();
        Properties prop = app.getProperties();
        if (tokenUtil.verifyToken(ISSUER, prop.getProperty("subject"), token)) {
            response.sendRedirect("/dashboard");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
