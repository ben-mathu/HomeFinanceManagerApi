<%
    String pageTitle = "Dashboard";
    String icon = "nav_menu_bar.png";

    pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("dashboard", "static/js/dashboard/dashboard.js");
    pageContext.setAttribute("settings", "static/js/dashboard/settings.js");
    pageContext.setAttribute("schedule", "static/js/dashboard/schedule.js");
    pageContext.setAttribute("income", "static/js/dashboard/income.js");
    pageContext.setAttribute("grocery", "static/js/dashboard/grocery.js");
    pageContext.setAttribute("household", "static/js/dashboard/household.js");
%>
<%@ include file = "../page_setting_top.jsp" %>
    <%@ include file = "../header.jsp" %>
    <%@ include file = "../nav_bar.jsp" %>
    <div class="container" >
        <%@ include file = "main_content.jsp" %>
        <%@ include file = "settings.jsp" %>
    </div>
    <%@ include file = "modal_add_grocery.jsp" %>
    <%@ include file = "modal_add_schedule.jsp" %>
    <%@ include file = "modal_add_income.jsp" %>
<%@ include file = "../page_setting_bottom.jsp" %>