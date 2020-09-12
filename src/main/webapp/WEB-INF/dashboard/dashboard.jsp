<%
    String pageTitle = "Dashboard";
    String icon = "nav_menu_bar.png";

    pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("piechart", "static/js/utils/piechart.js");
    pageContext.setAttribute("dashboard", "static/js/dashboard/dashboard.js");
    pageContext.setAttribute("settings", "static/js/dashboard/settings.js");
    pageContext.setAttribute("schedule", "static/js/dashboard/schedule.js");
    pageContext.setAttribute("income", "static/js/dashboard/income.js");
    pageContext.setAttribute("grocery", "static/js/dashboard/grocery.js");
    pageContext.setAttribute("household", "static/js/dashboard/household.js");
    pageContext.setAttribute("jars", "static/js/dashboard/money_jar.js");
    pageContext.setAttribute("expenses", "static/js/dashboard/expenses.js");
    pageContext.setAttribute("members", "static/js/dashboard/members.js");
    pageContext.setAttribute("payments", "static/js/dashboard/payments.js");
    pageContext.setAttribute("budgets", "static/js/dashboard/budgets.js");
    pageContext.setAttribute("transactions", "static/js/dashboard/transactions.js");
    pageContext.setAttribute("linegraph", "static/js/utils/line_graph.js");
    pageContext.setAttribute("reports", "static/js/dashboard/reports.js");
    pageContext.setAttribute("chart", "static/dist/Chart.js");
    pageContext.setAttribute("jquery", "static/dist/jquery.js");
    pageContext.setAttribute("jason2html", "static/dist/json2html.js");
%>
<%@ include file = "../page_setting_top.jsp" %>
    <div class="container">
        <%@ include file = "../header.jsp" %>
        <%@ include file = "../nav_bar.jsp" %>
        <%@ include file = "main_content.jsp" %>
        <%@ include file = "settings.jsp" %>
        <%@ include file = "messages.jsp" %>
        <%@ include file = "members.jsp" %>
        <%@ include file = "guide.jsp" %>
        <%@ include file = "modal_add_jar.jsp" %>
        <%@ include file = "modal_add_grocery.jsp" %>
        <%@ include file = "modal_add_schedule.jsp" %>
        <%@ include file = "modal_add_income.jsp" %>
        <%@ include file = "modal_add_expenses.jsp" %>
        <%@ include file = "modal_add_budget.jsp" %>
        <%@ include file = "modal_report.jsp" %>

        <%-- Dialogs --%>
        <%@ include file = "dialogs/payment_due_dialog.jsp" %>
        <%@ include file = "dialogs/message_dialog.jsp" %>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>