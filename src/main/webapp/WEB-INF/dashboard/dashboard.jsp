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
        <div class="content-container" >
            <div class="chart-chat-section" >
                <div class="chart-container rounded-corner shadow-1pt" >
                    <h4>Chart</h4>
                </div>
                <div class="chat-container rounded-corner shadow-1pt" >
                    <h4>Chat</h4>
                </div>
            </div>
            
            <div class="grocery-members-container" >
                <div class="members rounded-corner shadow-1pt" >
                    <h4>Members</h4>
                </div>
                <div class="expenses-section" >
                    <div class="grocery-subsection rounded-corner shadow-1pt" >
                        <div class="grocery-list" >
                            <h4>Grocery</h4>
                        </div>
                        <div class="grocery-schedule" >
                            <h4>Schedule</h4>
                        </div>
                    </div>
                    
                    <div class="transaction rounded-corner shadow-1pt" >
                        <h4>Transaction</h4>
                    </div>
                </div>
            </div>
            
            <div class="income rounded-corner shadow-1pt" >
                <h4>Savings</h4>
            </div>
            
            <div class="expenses rounded-corner shadow-1pt" >
                <h4>Expenses</h4>
            </div>
        </div>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>