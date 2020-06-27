let incomeSpan;
let btnOpenIncomeModal;
let btnCancelIncomeModal;

let btnAddIncome;

function configureIncomeElements() {

    incomeSpan = document.getElementById("income");
    btnOpenIncomeModal = document.getElementById("openIncomeModal");
    
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