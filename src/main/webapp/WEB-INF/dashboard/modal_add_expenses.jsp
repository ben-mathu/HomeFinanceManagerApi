<div id="expensesModal" class="modal">
    <div class="modal-content" >
        <h4 class="modal-title">Add Item to Grocery</h4>
        <p id="expense-modal-error" class="error-text"></p>
        <form>
            <div>
                <input id="expenseName" class="input-style" type="text" name="expense_name" placeholder="Expense Name" />
            </div>
            <div>
                <input id="expenseDesc" class="input-style" type="text" name="expense_description" placeholder="Description" />
            </div>
            <div>
                <input id="expenseAmount" class="input-style" type="text" name="expense_amount" placeholder="Amount" />
            </div>
            <div>
                <input id="payeeName" class="input-style" type="text" name="payee-name" placeholder="Payee Name" />
            </div>
            <div class="radio-container">
                <input id="business" class="radio-style" type="radio" name="payee_entity" value="Business" />
                <label for="#business">Business</label>
            </div>
            <div class="radio-container" >
                <input id="employee" class="radio-style" type="radio" name="payee_entity" value="Employee" />
                <label for="#employee">Employee</label>
            </div>
            <div id="businessNumber">
                <input id="payeeBusinessNumber" class="input-style" type="text" name="payeeBusinessNumber" placeholder="Business Number" />
            </div>
            <div id="phone" hidden>
                <input id="payeePhone" class="input-style" type="text" name="payeePhone" placeholder="Payee Phone Number" />
            </div>
            <div>
                <input id="addExpense" class="btn2" type="button" value="Submit" />
                <input id="closeExpenseModal" class="btn4-caution" type="button" value="Cancel">
            </div>
        </form>
    </div>
</div>