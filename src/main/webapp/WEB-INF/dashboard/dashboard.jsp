<%
    String pageTitle = "Dashboard";
    String scripts = "static/js/dashboard.js";
    pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("script", scripts);
%>
<%@ include file = "../page_setting_top.jsp" %>

<%@ include file = "../page_setting_bottom.jsp" %>