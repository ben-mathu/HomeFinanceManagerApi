<div id="jarModal" class="modal">
    <div class="modal-content elements-modal-inline" >
        <input id="moneyJarIdModal" type="text" name="jar-id" placeholder="Money Jar ID" hidden>
        <div class="jar-elements" >
            <h4 class="modal-title">Add a Money Jar</h4>
            <p id="jarModalError" class="error-text"></p>
            <div class="jar-container jar-elements" >
                <%-- name --%>
                <div >
                    <input id="jarLabel" class="input-style" type="text" name="name" placeholder="Money Jar Label">
                </div>
                <div >
                    <select id="categorySelector" class="select-style">
                        <option value="Groceries">Groceries</option>
                        <option value="Expenses">Expenses</option>
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
                    <div >
                        Get notified every? <select id="daySelector" class="select-style">
                            <option value="Mon">Monday</option>
                            <option value="Tuesday">Tuesday</option>
                            <option value="Wednesday">Wednesday</option>
                            <option value="Thursday">Thursday</option>
                            <option value="Friday">Friday</option>
                            <option value="Saturday">Saturday</option>
                            <option value="Sunday">Sunday</option>
                        </select>
                    </div>
                </div>
                <div id="timePicker" class="number-input">
                    Time: <input id="scheduledHour" class=" date-time-input" type="time" placeholder="00" max="23" />
                </div>
                <div id="datePicker" class="number-input">
                    Date: <input id="scheduledDate" class="date-time-input" type="date" name="date" placeholder="00" />
                </div>
            </div>
            <div class="jar-items jar-elements" >
                <div id="groceries" class="grocery-list" >
                    <div id="empty" hidden>
                        Your grocery list is empty.
                    </div>
                    <div id="groceryContainer" >
                        <%@ include file = "../template/groceries.jsp" %>
                    </div>
                    <input id="btnOpenGroceryModal" class="btn2" type="button" value="+ Add Item">
                </div>
                <div id="expense" class="grocery-list" hidden>
                    <div id="expenseContainer" >
                        <%@ include file = "../template/expense.jsp" %>
                    </div>
                    <input id="btnOpenExpenseModal" class="btn2" type="button" value="Add Expense Details">
                    <input id="btnEditExpenseModal" class="btn2" type="button" value="Edit Expense Details" hidden>
                </div>
            </div>
        </div>
        <div class="btn-submit">
            <input id="btnSaveJar" class="btn2 btn-right" type="button" value="Submit">
            <input id="cancelJarModal" class="btn4-caution" type="button" value="Cancel">
        </div>
    </div>
</div>