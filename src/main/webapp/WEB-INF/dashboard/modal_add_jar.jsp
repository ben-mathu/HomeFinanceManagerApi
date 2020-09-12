<div id="jarModal" class="modal">
    <div class="modal-content elements-modal-inline" >
        <input id="moneyJarIdModal" type="text" name="jar-id" placeholder="Money Jar ID" hidden>
        <div class="" >
            <h4 id="modalTitle" class="modal-title">Add Expense(Reminder)</h4>
            <p id="jarModalError" class="error-text"></p><span id="removeErrorMessage" style="background-color: #3F3A4B" hidden>x</span>
            <div class="jar-container jar-elements" >
                <div>
                    <span for="expenseType"></span>
                    <label for="expenseType">Expense</label></br>
                    <select id="expenseType" class="select-style">
                        <option value="--Select Expense Type--">--Select Expense Type--</option>
                    </select>
                </div>
                <div>
                    <label for="categorySelector">Expense Type</label></br>
                    <select id="categorySelector" class="select-style">
                        <option value="List">List</option>
                        <option value="Single Item">Single Item</option>
                    </select>
                </div>
                <div >
                    <h5>Total Amount: <span style="font-size: 26px">Ksh. </span><span id="totalAmount" style="font-size: 26px">0</span></h5>
                </div>
                <div >
                    <div >
                        <select id="scheduleSelector" class="select-style">
                            <option value="Scheduled">Scheduled Once</option>
                            <option value="Daily">Daily</option>
                            <option value="Weekly">Weekly</option>
                            <option value="Monthly">Monthly</option>
                        </select>
                    </div>
                </div>

                <%-- Select day of the week --%>
                <div id="dayOfWeek" hidden>
                    <label for="daySelector">Get notified every:</label>
                    <div >
                        <select id="daySelector" class="select-style">
                            <option value="Sunday">Sunday</option>
                            <option value="Monday">Monday</option>
                            <option value="Tuesday">Tuesday</option>
                            <option value="Wednesday">Wednesday</option>
                            <option value="Thursday">Thursday</option>
                            <option value="Friday">Friday</option>
                            <option value="Saturday">Saturday</option>
                        </select>
                    </div>
                </div>
                <div id="timePicker" class="number-input">
                    <span for="scheduledHour"></span>
                    Time: <input id="scheduledHour" class=" date-time-input" type="time" placeholder="00" max="23" />
                </div>
                <div id="datePicker" class="number-input">
                    <span for="scheduledDate"></span>
                    Date: <input id="scheduledDate" class="date-time-input" type="date" name="date" placeholder="00" />
                </div>
            </div>
            <div class="jar-items jar-elements" >
                <div id="groceries" class="grocery-list" hidden>
                    <div id="empty">
                        <span for="groceryContainer"></span>
                    </div>
                    <div id="groceryContainer" >
                        <table id="groceryItems">
                            <thead>
                                <tr>
                                    <th></th>
                                    <th>Name</th>
                                    <th>Price</th>
                                    <th>Description</th>
                                    <th>Remaining</th>
                                    <th>Required</th>
                                </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                    <input id="btnOpenGroceryModal" class="btn2" type="button" value="+ Add Item">
                    <div id="Paybill" class="payee-paybill">
                        <span for="fldPaybill"></span>
                        <input id="fldPaybill" class="input-style" type="number" placeholder="Paybill (Optional)" />
                    </div>
                </div>
                <div id="expense" class="expense-container">
                    <div class="expense-amount">
                        <span for="expenseAmount"></span>
                        <input id="expenseAmount" class="input-style label-block-style" type="number" name="expense_amount" placeholder="300" />
                    </div>
                    <div>
                        <span for="payeeName"></span>
                        <label for="payeeName">Payee Name (Section only supported in Kenya)</label></br>
                        <select id="payeeName" class="select-style">
                            <option value="--Select Expense Type--">--Select Expense Type--</option>
                        </select>
                    </div>
                    <div id="businessNumber">
                        <span for="payeeBusinessNumber"></span>
                        <input id="payeeBusinessNumber" class="input-style" type="number" name="business_number" placeholder="Business Number (Optional)" />
                    </div>
                    <div id="account">
                        <span for="payerAccountNumber"></span>
                        <input id="payerAccountNumber" class="input-style" type="number" name="account_number" placeholder="Payer Account Number (Optional)" />
                    </div>
                    <div id="expenseContainer" >
                        <%@ include file = "../template/expense.jsp" %>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-submit">
            <input id="btnSaveJar" class="btn2" type="button" value="Submit">
            <input id="cancelJarModal" class="btn4-caution" type="button" value="Cancel">
            <div class="expense-editors btn-right">
                <input id="btnEditExpense" class="btn2" type="button" value="Edit" hidden/>
                <input id="btnDeleteExpense" class="btn3-warn" type="button" value="Delete" hidden/>
            </div>
        </div>
    </div>
</div>