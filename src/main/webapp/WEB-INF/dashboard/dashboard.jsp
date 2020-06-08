<%
    String pageTitle = "Dashboard";
    String scripts = "static/js/dashboard.js";
    String icon = "nav_menu_bar.png";

    pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("script", scripts);
    pageContext.setAttribute("timer", "static/js/dashboard/dashboard.js");
%>
<%@ include file = "../page_setting_top.jsp" %>
    <%@ include file = "../header.jsp" %>
    <%@ include file = "../nav_bar.jsp" %>

<%@ include file = "../page_setting_bottom.jsp" %>