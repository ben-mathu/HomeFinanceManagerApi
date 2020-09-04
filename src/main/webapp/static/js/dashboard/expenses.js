let rdPersonal;
let rdHouseHold;
let rdEmployee;

let businessNumber;
let payeeAccountNumber;

/**
 * Expenses Input Fields
 */
let expenseAmount;
let payeeName;
let expenseId;
let expenseTemplate;

/**
 * Expense Types
 */
//const expenseTypes = {
//    PERSONAL: "Personal",
//    HOUSEHOLD: "Household",
//    EMPLOYEE: "Employee"
//};

let expenseGlobal = {
    expense_id: "",
    amount: "",
    payee_name: "",
    business_number: "",
    account_number: ""
};

let payeeNames = {
    'KPLC': '888888',
    'Kiambu Water and sewage Company': '885100',
    'Testing': '174379',
    'Other': ''
};

function configureExpenses() {
    expenseTemplate = document.getElementById("expenseTemplate");
    
    expenseId = document.getElementById("expenseId");
    expenseAmount = document.getElementById("expenseAmount");
    expenseAmount.addEventListener("input", function(event) {
        let spanAmount = document.querySelector("span[for='expenseAmount']");
        spanAmount.textContent = "";
        spanAmount.style.display = "none";
        
        expenseGlobal.amount = event.target.value;
        onRowLoadedExpenses(expenseGlobal.amount);
    });
    
    payeeName = document.getElementById("payeeName");
    let payeeNamesList = Object.keys(payeeNames);
    payeeNamesList.forEach(name => {
        let option = document.createElement("option");
        option.value = name;
        option.innerHTML = name;
        payeeName.appendChild(option);
    });
    
    businessNumber = document.getElementById("payeeBusinessNumber");
    payeeName.addEventListener("change", function(event) {
        expenseGlobal.payee_name = event.target.value;
        businessNumber.value = payeeNames[event.target.value];
        expenseGlobal.business_number = payeeNames[event.target.value];
    });
    
    businessNumber.addEventListener("input", function(event) {
        expenseGlobal.business_number = event.target.value;
    });
    
    businessNumber.addEventListener("change", function(event) {
        expenseGlobal.business_number = event.target.value;
    });
    
    payeeAccountNumber = document.getElementById("payerAccountNumber");
    payeeAccountNumber.addEventListener("input", function(event) {
        expenseGlobal.account_number = event.target.value;
    });

    let businessNumberContainer = document.getElementById("businessNumber");
    let accountNumberContainer = document.getElementsByName("account_number");
}

function setExpenseFields(jarId) {
    
    let expense;
    if (jarId !== "") {
        let jarDto = jars.getJar(jarId);
        expenseGlobal = jarDto.expense;
        expense = jarDto.expense;
    }
    
    expenseAmount.value = expense.amount;
    
    let payeeNameOptions = payeeName.options;
    for (var i = 0; i < payeeNameOptions.length; i++) {
        let option = payeeNameOptions[i];
        if (option.value === expense.payee_name) {
            payeeName.options[i].selected = true;
            break;
        }
    }
    
    businessNumber.value = expense.business_number;
    payeeAccountNumber.value = expense.account_number;
}

function setExpense(jarId, expense) {
    
    if (jarId !== "") {
        let jarDto = jars.getJar(jarId);
        expense = jarDto.expense;
    }

    let expenseTemplateClone = expenseTemplate.content.cloneNode(true);

    let amount = expenseTemplateClone.querySelector("#expAmount");
    amount.innerHTML = expense.amount;
    let expPayeeName = expenseTemplateClone.querySelector("#expPayeeName");
    expPayeeName.innerHTML = expense.payee_name;
    let busiNumber = expenseTemplateClone.querySelector("#expBusinessNumber");
    busiNumber.innerHTML = expense.business_number;
    let account = expenseTemplateClone.querySelector("#accountNumber");
    account.innerHTML = expense.account_number;

    return expenseTemplateClone;
}

//function updateExpense() {
//    let prevAmount = expenseAmount.value;
//
//    expenseGlobal.expense_id = expenseId.value;
//    expenseGlobal.expense_name = expenseName.value;
//    expenseGlobal.expense_description = expenseDesc.value;
//    expenseGlobal.amount = expenseAmount.value;
//    expenseGlobal.payee_name = payeeName.value;
//    
//    if (rdPersonal.checked) {
//        expenseGlobal.type = rdPersonal.value;
//        expenseGlobal.business_number = document.getElementById("payeeBusinessNumber").value;
//    } else if (rdHouseHold.checked) {
//        expenseGlobal.type = rdHouseHold.value;
//        expenseGlobal.business_number = document.getElementById("payeeBusinessNumber").value;
//        expenseGlobal.account_number = document.getElementById("payerAccountNumber").value;
//    } else if (rdEmployee.checked) {
//        expenseGlobal.type = rdEmployee.value;
//        expenseGlobal.account_number = document.getElementById("payerAccountNumber").value;
//    }
//
//    // set total amount
//    updateTotalAmount(prevAmount, expenseGlobal.amount);
//
//    // set expense
//    expenseItemsSection.innerHTML = "";
//    expenseItemsSection.appendChild(setExpense(moneyJarIdModal.value, expenseGlobal));
//}