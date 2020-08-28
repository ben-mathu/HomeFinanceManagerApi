<div id="incomeModal" class="modal">
    <div class="modal-content " >
        <h4 id="incomeModalTitle" class="modal-title">Add Income Balance(as of today)</h4>
        <p id="income-modal-error" class="error-text"></p>
        <div>
            Debit account type:</br>
            <select id="incomeType" class="select-style">
                <option value="Mpesa">Mpesa</option>
            </select>
        </div>
<!--        <div>
            <input id="incomeDescription" class="input-style" type="textarea" placeholder="Description eg. Employment" min="0" />
        </div>-->
        <div>
            <input id="incomeValue" class="input-style" type="number" placeholder="Amount, e.g 10000" min="0" />
        </div>
        <div>
            <input id="addIncome" class="btn2" type="button" value="Submit" />
            <input id="cancelIncomeModal" class="btn4-caution" type="button" value="Cancel">
        </div>
    </div>
</div>