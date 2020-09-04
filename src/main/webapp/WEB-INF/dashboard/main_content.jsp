<div id="mainContent" class="content-container">
    <input type="hidden" id="contextPath" name="context_path" value="<%= request.getContextPath() %>">
    <div class="chart-chat-section section-style" >
        <div class="chart-container rounded-corner shadow-1pt subsection-background" >
            <label for="#lineGraphCanvas"><i style="font-size: 16px; margin-bottom: 5px">Profit and Expenditure Line Graph</i></label>
            <canvas id="lineGraphCanvas"></canvas>
            <legend class="legends-for-piechart" for="#lineGraphCanvas"></legend>
        </div>
        <div class="chat-container rounded-corner shadow-1pt subsection-background" >
            <label for="#memberList"><i style="font-size: 16px; margin-bottom: 5px">Group Members</i></label>
            <div id="memberList" class="members" >
                <%@ include file = "../template/member.jsp" %>
            </div>
        </div>
    </div>
    
    <div class="grocery-members-container section-style" >
        <div class="notifications rounded-corner shadow-1pt subsection-background">
            <label for="#notificationContainer"><i style="font-size: 16px; margin-bottom: 5px">Notification of Expenses (Click to pay)</i></label>
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

                    <label for="#jarList"><i style="font-size: 16px; margin-bottom: 5px">Scheduled Expenses</i></label>
                    <div id="jarList" class="jar-list">
                        <%@ include file = "../template/jar.jsp" %>
                    </div>

                    <input id="btnOpenJarModal" class="btn2" type="button" value="+ Add Schedule">
                </div>
                <div class="jars-pie-chart" >
                    <canvas id="jarsCanvas"></canvas>
                    <legend class="legends-for-piechart" for="jarsCanvas"></legend>
                </div>
            </div>

            <div class="expenses rounded-corner shadow-1pt subsection-background" >
                <label for="#otherExpenditures"><i style="font-size: 16px; margin-bottom: 5px">Other Expenditures</i></label>
                <div id="otherExpenditures"></div>
            </div>
            
            <!-- This section displays transactions -->
            <div class="rounded-corner shadow-1pt subsection-background" >
                <label for=".transactionTable"><i style="font-size: 16px; margin-bottom: 5px">Transaction Log</i></label>
                <div class="transaction transactions-table">
                    <table id="transactionTable">
                        <thead>
                            <tr>
                                <th>Index</th>
                                <th>Transaction Description</th>
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