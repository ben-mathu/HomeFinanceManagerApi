<%
    String pageTitle = "Dashboard";
    String icon = "nav_menu_bar.png";

    pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("dashboard", "static/js/dashboard/dashboard.js");
%>
<%@ include file = "../page_setting_top.jsp" %>
    <%@ include file = "../header.jsp" %>
    <%@ include file = "../nav_bar.jsp" %>
    <div class="container" >
        <%@ include file = "main_content.jsp" %>
        <%@ include file = "settings.jsp" %>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>