let rdPersonal;
let rdHouseHold;
let rdEmployee;

let businessNumber;
let payeeNumber;

let btnOpenExpenseModal;
let btnCloseExpenseModal;
let btnSaveExpense;

let expense = {
    expense_id: "",
    expense_name: "",
    expense_description: "",
    amount: "",
    payee_name: "",
    type: "",
    business_number: "",
    phone_number: ""
}

function configureExpenses() {
    businessNumber = document.getElementById("payeeBusinessNumber");
    payeeNumber = document.getElementById("payeePhone");

    // checked checked radio button
    rdPersonal = document.getElementById("personal");
    rdHouseHold = document.getElementById("householdExpense");
    rdEmployee = document.getElementById("employee");
    rdPersonal.checked = true;

    let businessNumberContainer = document.getElementById("businessNumber");
    let phoneNumberContainer = document.getElementById("phone");

    rdPersonal.onchange = function() {
        phoneNumberContainer.hidden = true;
        businessNumberContainer.hidden = false;
    }

    rdHouseHold.onchange = function() {
        phoneNumberContainer.hidden = true;
        businessNumberContainer.hidden = false;
    }

    rdEmployee.onchange = function() {
        phoneNumberContainer.hidden = false;
        businessNumberContainer.hidden = true;
    }

    btnOpenExpenseModal = document.getElementById("btnOpenExpenseModal");
    btnOpenExpenseModal.onclick = function() {
        openExpenseModal();
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
    // get items
    expense.expense_name = document.getElementById("expenseName").value;
    expense.expense_description = document.getElementById("expenseDesc").value;
    expense.amount = document.getElementById("expenseAmount").value;
    expense.payee_name = document.getElementById("payeeName").value;
    
    if (rdPersonal.checked) {
        expense.type = rdPersonal.value;
        expense.business_number = document.getElementById("payeeBusinessNumber").value;
    } else if (rdHouseHold.checked) {
        expense.type = rdHouseHold.value;
        expense.business_number = document.getElementById("payeeBusinessNumber").value;
    } else if (rdEmployee.checked) {
        expense.type = rdEmployee.value;
        expense.phone_number = document.getElementById("payeePhone").value;
    }
    // set expense
    setExpense();
}

function setExpense() {
    closeExpenseModal();

    let name = document.getElementById("expName");
    name.innerHTML = expense.expense_name;
    let desc = document.getElementById("expDesc");
    desc.innerHTML = expense.expense_description;
    let amount = document.getElementById("expAmount");
    amount.innerHTML = expense.amount;
    let payeeName = document.getElementById("expPayeeName");
    payeeName.innerHTML = expense.payee_name;
    let busiNumber = document.getElementById("expBusinessNumber");
    busiNumber.innerHTML = expense.business_number;
    let phone = document.getElementById("payeePhoneNum");
    phone.innerHTML = expense.phone_number;

    // set total amount
    onRowLoaded(expense.amount);
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