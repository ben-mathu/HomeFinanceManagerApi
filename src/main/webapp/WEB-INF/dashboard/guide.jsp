<div id="Guide" hidden>
    <div class="guide-container">
        <div class="introduction-guide">
            <strong>Introduction</strong><br>
            A household is a unit representing more than one individual who reside
            in the same house and share amenities, such as utensils, meals or furniture.
            Each individual may contribute to expenses, monthly expenses or daily
            expenses. This system aims to assist with the monthly expenditure members
            incur. Also each individual can be able to see information in regards to their
            contribution.
        </div>
        <div class="instructions-style">
            <div class="guide-content">
                After login, you are redirected to the dashboard where you are 
                taken through a couple of modals. First you should add income, 
                which can be cancelled to be done at a later time.
            </div>
            <img class="image-instr-style-right" src="<%= request.getContextPath() %>/static/images/add_income.png" width="500px" height="250px"/>
        </div>
        
        <div class="instructions-style">
            <img class="image-instr-style-left" src="<%= request.getContextPath() %>/static/images/add_expense.png" width="500px" height="250px"/>
            <div class="guide-content">
                The next prompt you asks you to add an expense, this can also be 
                cancelled and can be done at a later time.
            </div>
        </div>

        <div class="instructions-style">
            <div class="guide-content">
                <strong>Dashboard</strong><br>
                The dashboard is a part of the user interface that displays processed
                data and information retrieved from the database. The dashboard design
                provides you an at-a-glance view of key performance indicators (KPIs),
                in this case user expenditure and monthly profit, expenses 
                (paid or unpaid expenses), pie chart showing how much each 
                expense contributes to the total expenses amount; and transactions.
            </div>
            <img class="image-instr-style-right" src="<%= request.getContextPath() %>/static/images/dashboard_top.png" width="500px" height="250px"/>
        </div>

        <div class="instructions-style">
            <img class="image-instr-style-left" src="<%= request.getContextPath() %>/static/images/01_transactions.png" width="500px" height="250px"/>
            <div class="guide-content">
                <strong>Perform Transactions (Paying for expenses)</strong><br>
                As easily as adding expense schedules, the notification area lists 
                expenses that the user has already been notified and/or missed 
                the notification that shows at the bottom right of the dashboard.
            </div>
        </div>

        <div class="instructions-style">
            <div class="guide-content">
                Click on any of the expenses to transact or pay for your expense.
                You can also perform a transaction when you click the accept button 
                on the payment notification in the bottom right section of the application, 
                as illustrated in the image above.
                Image show an illustration of the notified list of expenses.
            </div>
            <img class="image-instr-style-right" src="<%= request.getContextPath() %>/static/images/02_transactions.png" width="500px" height="250px"/>
        </div>

        <div class="instructions-style">
            <img class="image-instr-style-left" src="<%= request.getContextPath() %>/static/images/account_settings.png" width="500px" height="250px"/>
            <div class="guide-content">
                <strong>Account settings</strong><br>
                The account settings can be found when you 
                select the gear icon in the left section of the application 
                (the navigation bar). You can add, delete and manage their account 
                and application settings from this area.
            </div>
        </div>

        <div class="instructions-style">
            <div class="guide-content">
                The user can also share their household invite code so that users from 
                their household can join. 
            </div>
            <img class="image-instr-style-right" src="<%= request.getContextPath() %>/static/images/household_invite.png" width="500px" height="250px"/>
        </div>
    </div>
</div>
