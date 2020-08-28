<div id="mainContent" class="content-container">
    <input type="hidden" id="contextPath" name="context_path" value="<%= request.getContextPath() %>">
    <div class="chart-chat-section section-style" >
        <div class="chart-container rounded-corner shadow-1pt subsection-background" >
            <canvas id="lineGraphCanvas"></canvas>
            <legend class="legends-for-piechart" for="lineGraphCanvas"></legend>
        </div>
        <div class="chat-container rounded-corner shadow-1pt subsection-background" >
            <div id="notificationContainer" class="notifications">
                <%@ include file = "../template/notification.jsp" %>
            </div>
        </div>
    </div>
    
    <div class="grocery-members-container section-style" >
        <div id="memberList" class="members rounded-corner shadow-1pt subsection-background" >
            <%@ include file = "../template/member.jsp" %>
        </div>
        
        <div class="expenses-section" >
            <div class="grocery-subsection rounded-corner shadow-1pt subsection-background" >
                <div class="jar-list-container" >
                    <%-- Create a table of scheduled money jar labels and scheduled date. --%>
                    <div id="emptyExpenseList" class="empty"  hidden>
                        Your expenses will be listed here
                    </div>

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

            <div class="transaction rounded-corner shadow-1pt subsection-background" >
                <h4>Regular Expenditure</h4>
            </div>
        </div>
    </div>
    
    <div class="expenses rounded-corner shadow-1pt subsection-background" >
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