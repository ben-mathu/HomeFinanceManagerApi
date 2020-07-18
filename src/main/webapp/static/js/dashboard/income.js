let incomeSpan;
let btnOpenIncomeModal;

let incomeModalTitle;
let btnAddIncome;
let btnCancelIncomeModal;

let incomeTypeSelector;

const incomeTypeFields = {
    MOB_MONEY: 'Mobile Money',
    BANK_ACC: 'Bank Account'
}

function configureIncomeElements() {

    incomeSpan = document.getElementById("income");
    btnOpenIncomeModal = document.getElementById("openIncomeModal");
    incomeModalTitle = document.getElementById("incomeModalTitle");
    
    btnOpenIncomeModal.onclick = function() {
        incomeModal.style.display = "block";
    }

    btnCancelIncomeModal = document.getElementById("cancelIncomeModal");
    btnCancelIncomeModal.onclick = function() {
        incomeModal.style.display = "none";
    }

    btnAddIncome = document.getElementById("addIncome");
    btnAddIncome.onclick = function() {
        addIncome();
    }

    // declare income type selector
    incomeTypeSelector = document.getElementById("incomeType");
}

function openIncomeModal(modalDetails) {
    btnAddIncome.value = modalDetails.button;
    incomeModal.style.display = "block";

    btnAddIncome.onclick = function() {
        updateIncome(modalDetails);
    }
}

function closeIncomeModal() {
    incomeModal.style.display = "none";
}

function updateIncome(callback) {
    var request = getXmlHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                // close modal
                closeIncomeModal();

                setIncome(JSON.parse(request.responseText));

                showIncome(user);

                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len == 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
            }
        }
    }

    var token = userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));
    var userId = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));


    let incomeType = incomeTypeSelector.options[incomeTypeSelector.selectedIndex].value;
    incomeType = incomeFields.ACCOUNT_TYPE + "=" + escape(incomeType);
    var incomeDesc = incomeFields.INCOME_DESC + "=" + escape(document.getElementById("incomeDescription").value);
    var incomeValue = incomeFields.AMOUNT + "=" + escape(document.getElementById("incomeValue").value);

    var data = userId + "&" + incomeType + "&" + incomeDesc + "&" + incomeValue + "&" + token;

    request.open("POST", ctx + "/dashboard/user-controller/add-income", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

function addIncome() {
    closeOptionsMenu();

    var request = getXmlHttpRequest();
    request.onreadystatechanged = function() {
        if (request.readyState == 4) {
            if (reaquest.status == 200) {
                setIncome(JSON.parse(request.responseText));

                showIncome(user);
            }
        }
    }

    var token = userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));
    var userId = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));

    var incomeDescription = incomeFields.ACCOUNT_TYPE + "=" + escape(document.getElementById("incomeDescription").value);
    var value = incomeFields.AMOUNT + "=" + escape(document.getElementById("incomeValue").value);

    var data = userId + "&" + incomeDescription + "&" + value + "&" + token;

    request.open("POST", ctx + "/dashboard/user-controller/add-income", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

/**
 * display income in the dropdown option menu if available
 * 
 * @param {Income} income Contain income details
 */
function showIncome(income) {
    if (income != null) {
        if (income.amount > 0) {
            incomeSpan.hidden = false;
            var text = incomeSpan.innerHTML + user.income;
            incomeSpan.innerHTML = text;
            
            btnOpenIncomeModal.hidden = true;
            incomeSpan.value = income.amount;
        }
    }
}