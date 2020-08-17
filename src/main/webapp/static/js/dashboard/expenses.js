let rdPersonal;
let rdHouseHold;
let rdEmployee;

let businessNumber;
let payeeAccountNumber;

let btnOpenExpenseModal;
let btnEditExpenseModal;
let btnCloseExpenseModal;
let btnSaveExpense;

/**
 * Expenses Input Fields
 */
let expenseName;
let expenseDesc;
let expenseAmount;
let payeeName;
let expenseId;

/**
 * Expense Types
 */
const expenseTypes = {
    PERSONAL: "Personal",
    HOUSEHOLD: "Household",
    EMPLOYEE: "Employee"
}

function configureExpenses() {
    expenseId = document.getElementById("expenseId");
    expenseName = document.getElementById("expenseName");
    expenseDesc = document.getElementById("expenseDesc");
    expenseAmount = document.getElementById("expenseAmount");
    payeeName = document.getElementById("payeeName");
    
    businessNumber = document.getElementById("payeeBusinessNumber");
    payeeAccountNumber = document.getElementById("payerAccountNumber");

    // checked checked radio button
    rdPersonal = document.getElementById("personal");
    rdHouseHold = document.getElementById("householdExpense");
    rdEmployee = document.getElementById("employee");
    rdPersonal.checked = true;

    let businessNumberContainer = document.getElementById("businessNumber");
    let accountNumberContainer = document.getElementsByName("account_number");

    rdPersonal.onchange = function() {
        if (rdPersonal.checked) {
            accountNumberContainer[0].placeholder = "Account Number";
            businessNumberContainer.hidden = false;
        }
    }

    rdHouseHold.onchange = function() {
        if (rdHouseHold.checked) {
            accountNumberContainer[0].placeholder = "Account Number";
            businessNumberContainer.hidden = false;
        }
    }

    rdEmployee.onchange = function() {
        if (rdEmployee.checked) {
            accountNumberContainer[0].placeholder = "Phone Number";
            businessNumberContainer.hidden = true;
        }
    }

    btnOpenExpenseModal = document.getElementById("btnOpenExpenseModal");
    btnOpenExpenseModal.onclick = function() {
        openExpenseModal();
    }

    btnEditExpenseModal = document.getElementById("btnEditExpenseModal");
    btnEditExpenseModal.onclick = function () {
        openExpenseModalForEdit(moneyJarIdModal.value);
    }

    // Initialize and define click event to close expense modal
    btnCloseExpenseModal = document.getElementById("closeExpenseModal");
    btnCloseExpenseModal.onclick = function() {
        closeExpenseModal();
    }

    // declare submit button 
    btnSaveExpense = document.getElementById("addExpense");
    btnSaveExpense.onclick = function () {
        addExpense();
    }
}

function addExpense() {

    expense.expense_name = expenseName.value;
    expense.expense_description = expenseDesc.value;
    expense.amount = expenseAmount.value;
    expense.payee_name = payeeName.value;
    
    if (rdPersonal.checked) {
        expense.type = rdPersonal.value;
        expense.business_number = document.getElementById("payeeBusinessNumber").value;
    } else if (rdHouseHold.checked) {
        expense.type = rdHouseHold.value;
        expense.business_number = document.getElementById("payeeBusinessNumber").value;
        expense.account_number = document.getElementById("payerAccountNumber").value;
    } else if (rdEmployee.checked) {
        expense.type = rdEmployee.value;
        expense.account_number = document.getElementById("payerAccountNumber").value;
    }

    // set total amount
    onRowLoaded(expense.amount);

    // set expense
    expenseItemsSection.innerHTML = "";
    expenseItemsSection.appendChild(setExpense(""));
}

function setExpense(jarId) {
    closeExpenseModal();
    let jarDto = jars.getJar(jarId);
    
    jarDto.expense = expense;

    let expenseTemplate = document.getElementById("expenseTemplate");

    let name = expenseTemplate.content.querySelector("#expName");
    name.innerHTML = expense.expense_name;
    let desc = expenseTemplate.content.querySelector("#expDesc");
    desc.innerHTML = expense.expense_description;
    let amount = expenseTemplate.content.querySelector("#expAmount");
    amount.innerHTML = expense.amount;
    let expPayeeName = expenseTemplate.content.querySelector("#expPayeeName");
    expPayeeName.innerHTML = expense.payee_name;
    let busiNumber = expenseTemplate.content.querySelector("#expBusinessNumber");
    busiNumber.innerHTML = expense.business_number;
    let account = expenseTemplate.content.querySelector("#accountNumber");
    account.innerHTML = expense.account_number;

    let expenseTemplateClone = document.importNode(expenseTemplate.content, true);

    btnOpenExpenseModal.hidden = true;
    btnEditExpenseModal.hidden = false;

    return expenseTemplateClone;

}

/**
 * Close expense Modal
 */
function closeExpenseModal() {
    expensesModal.style.display = "none";
}

/**
 * Open Expense Modal
 */
function openExpenseModal() {
    expensesModal.style.display = "block";
}

/**
 * 
 * @param {String} jarId key represents an element in moneyJarList
 */
function openExpenseModalForEdit(jarId) {
    expensesModal.style.display = "block";

    let jarDto = jars.getJar(jarId);

    let expense = jarDto.expense;

    expenseId.value = expense.expense_id;
    expenseName.value = expense.expense_name;
    expenseDesc.value = expense.expense_description;
    expenseAmount.value = expense.amount;
    payeeName.value = expense.payee_name;
    businessNumber.value = expense.business_number;
    payeeAccountNumber.value = expense.account_number;

    if (expense.type == null) {
        rdHouseHold.checked = true;
    } else if (expense.type == expenseTypes.HOUSEHOLD) {
        rdHouseHold.checked = true;
    } else if (expense.type == expenseTypes.PERSONAL) {
        rdPersonal.checked = true;
    } else if (expense.type == expenseTypes.EMPLOYEE) {
        rdEmployee.checked = true;
    }

    btnSaveExpense.onclick = function () {
        updateExpense()
    }
}

function updateExpense() {
    let prevAmount = expenseAmount.value;

    expense.expense_id = expenseId.value;
    expense.expense_name = expenseName.value;
    expense.expense_description = expenseDesc.value;
    expense.amount = expenseAmount.value;
    expense.payee_name = payeeName.value;
    
    if (rdPersonal.checked) {
        expense.type = rdPersonal.value;
        expense.business_number = document.getElementById("payeeBusinessNumber").value;
    } else if (rdHouseHold.checked) {
        expense.type = rdHouseHold.value;
        expense.business_number = document.getElementById("payeeBusinessNumber").value;
        expense.account_number = document.getElementById("payerAccountNumber").value;
    } else if (rdEmployee.checked) {
        expense.type = rdEmployee.value;
        expense.account_number = document.getElementById("payerAccountNumber").value;
    }

    // set total amount
    updateTotalAmount(prevAmount, expense.amount);

    // set expense
    expenseItemsSection.innerHTML = "";
    expenseItemsSection.appendChild(setExpense(moneyJarIdModal.value));
}