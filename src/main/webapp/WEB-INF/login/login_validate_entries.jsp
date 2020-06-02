<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.miiguar.hfms.data.models.user.model.User"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="com.miiguar.hfms.utils.Constants"%>
<%
    String emailResponse = "";
    String usernameResponse = "";
    String passwordResponse = "";
    String resp = "";
    
	boolean validParams = true;
    if (request.getMethod().toUpperCase().equals("POST")) {
        String username = request.getParameter("username") == null ? "" : request.getParameter("username").trim();
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");

        if (username.isEmpty()) {
            usernameResponse = "Username is required!";
            validParams = false;
        }
        
        if (validParams) {
            String validity = Constants.isPasswordValid(password);
            if (validity.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK);
                request.getRequestDispatcher("register-user").forward(request, response);
            } else {
                passwordResponse = "You password should have these properties:</br>" + validity;
            }
        }
    }
%>