let radioBusiness;
let radioEmployee;
let businessNumber;
let payeeNumber;

let btnOpenExpenseModal;
let btnCloseExpenseModal;

let expense = {
    _id: "",
    name: "",
    desc: "",
    amount: "",
    payee: "",
    business_number: "",
    phone_number: ""
}

function configureExpenses() {
    businessNumber = document.getElementById("payeeBusinessNumber");
    payeeNumber = document.getElementById("payeePhone");

    radioBusiness = document.getElementById("business");
    radioEmployee = document.getElementById("employee");
    radioBusiness.checked = true;

    let businessNumberContainer = document.getElementById("businessNumber");
    let phoneNumberContainer = document.getElementById("phone");

    radioBusiness.onchange = function() {
        phoneNumberContainer.hidden = true;
        businessNumberContainer.hidden = false;
    }

    radioEmployee.onchange = function() {
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
}

function closeExpenseModal() {
    expensesModal.style.display = "none";
}

function openExpenseModal() {
    expensesModal.style.display = "block";
}