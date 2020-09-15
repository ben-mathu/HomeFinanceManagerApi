<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<div id="mainContent" class="content-container">
    <input type="hidden" id="contextPath" name="context_path" value="<%= request.getContextPath() %>">
    <div class="chart-chat-section section-style" >
        <div id="chartContainer" class="chart-container rounded-corner shadow-1pt subsection-background" >
            <label for="lineGraphCanvas"><i style="font-size: 16px; margin-bottom: 5px">Expenditure Line Graph</i></label>
            <div class="chart-element">
                <canvas id="lineGraphCanvas" class="line-graph-canvas"></canvas>
                <legend for="lineGraphCanvas" class="legends-for-linegraph"></legend>
            </div>
        </div>
        <div class="chat-container rounded-corner shadow-1pt subsection-background" >
            <label for="memberList"><i style="font-size: 16px; margin-bottom: 5px">Group Members</i></label>
            <div id="memberList" class="members" ></div>
        </div>
    </div>
    
    <div class="grocery-members-container section-style" >
        <div class="notifications rounded-corner shadow-1pt subsection-background">
            <label for="notificationContainer"><i style="font-size: 16px; margin-bottom: 5px">Notification of Expenses (Click to pay)</i></label>
            <div id="notificationContainer" class="notification-list">
                <%@ include file = "../template/notification.jsp" %>
            </div>
        </div>
        <div class="expenses-section" >
            <div class="grocery-subsection rounded-corner shadow-1pt subsection-background" >
                
                <div id="scheduleSubsection" class="jar-list-container" >
                    <%-- Create a table of scheduled money jar labels and scheduled date. --%>
                    <div id="emptyExpenseList" class="empty"  hidden>
                        Your expenses will be listed here
                    </div>

                    <label for="jarList"><i style="font-size: 16px; margin-bottom: 5px">Scheduled Expenses</i></label>
                    <div id="jarList" class="jar-list">
                        <%@ include file = "../template/jar.jsp" %>
                    </div>

                    <input id="btnOpenJarModal" class="btn2" type="button" value="Add Expense Schedule">
                </div>
                <div class="jars-pie-chart" >
                    <canvas id="jarsCanvas"></canvas>
                    <legend class="legends-for-piechart" for="jarsCanvas"></legend>
                </div>
            </div>

<!--            <div class="expenses rounded-corner shadow-1pt subsection-background" >
                <label for="otherExpenditures"><i style="font-size: 16px; margin-bottom: 5px">Other Expenditures</i></label>
                <div id="otherExpenditures"></div>
            </div>-->
            
            <!-- This section displays transactions -->
            <div class="rounded-corner shadow-1pt subsection-background" >
                <label for="transactionTable" style="margin-right: 30%"><i style="font-size: 16px; margin-bottom: 5px">Transaction Log</i></label>
                From: <input id="dateFrom" class="input-style2" type="date" max="<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>" required/>
                To: <input id="dateTo" class="input-style2" type="date" max="<%= new SimpleDateFormat("yyyy-MM-dd").format(new Date()) %>" required/>
                <input id="btnReport" class="btn2" type="button" value="Submit"/>
                <div class="transaction transactions-table">
                    <table id="transactionTable">
                        <thead>
                            <tr>
                                <th>Index</th>
                                <th>Expense</th>
                                <th>Amount</th>
                                <th>Payment Status</th>
                                <th>Payment Timestamp</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file = "../template/member.jsp" %>