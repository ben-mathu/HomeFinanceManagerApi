<div id="budgetModal" class="modal" >
    <div class="modal-content" >
        <input type="text" name="budgetModalId" value="id" hidden>
        <div class="budget-element" >
            <div class="budget-month" >
                Budgeted Month: <span id="budgetMonth"></span>, <span id="budgetYear"></span>
            </div>
            <div class="budget-amount-container" >
                <div class="total-budget-amount" >
                    <h3>Total Amount:<span style="font-size: 23px;">KSH. <span id="budgetAmount">390</span></span></h3>
                </div>
                <%-- <input id="buttonChangeAmount" class="btn2" type="button" name="change" value="Change Amount"> --%>
            </div>
            <div>
                <input id="accountRef" class="input-style" type="text" name="account-number" placeholder="Account Number">
            </div>
            <div class="budget-expense-container" >
                <div id="budgetJarList" class="budget-expenses" ></div>
                <div class="add-expense-button" >
                    <input id="topUpWallet" class="btn2" type="button" name="top-up" value="Top Up Your Account">
                </div>
            </div>
        </div>
    </div>
</div>