<div id="mainContent" class="content-container" >
    <input type="hidden" id="contextPath" name="context_path" value="<%= request.getContextPath() %>">
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
                <div class="envelope-list" >
                    <%-- Create a table of scheduled envelopes name and scheduled date. --%>
                    <div class="empty"  hidden>
                        you can have all envelopes listed here
                    </div>

                    <table id="envelopeItems">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Date</th>
                                <th>Intervals</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>

                    <input id="btnOpenEnvelopeModal" class="btn2" type="button" value="+ Add Schedule">
                </div>
            </div>
            
            <div class="transaction rounded-corner shadow-1pt" >
                <h4>Regular Expenditure</h4>
            </div>
        </div>
    </div>
    
    <div class="income rounded-corner shadow-1pt" >
        <h4>Savings</h4>
    </div>
    
    <div class="expenses rounded-corner shadow-1pt" >
        <h4>Transactions</h4>
    </div>
</div>