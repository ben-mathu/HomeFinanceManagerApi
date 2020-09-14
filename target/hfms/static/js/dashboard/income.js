let incomeSpan;
let btnOpenIncomeModal;

let incomeModalTitle;
let btnAddIncome;
let btnCancelIncomeModal;

let incomeTypeSelector;

const incomeTypeFields = {
    MOB_MONEY: 'Mobile Money'
};

function configureIncomeElements() {

    incomeSpan = document.getElementById("income");
    btnOpenIncomeModal = document.getElementById("openIncomeModal");
    incomeModalTitle = document.getElementById("incomeModalTitle");
    
    btnOpenIncomeModal.onclick = function() {
        incomeModal.style.display = "block";
    };

    btnCancelIncomeModal = document.getElementById("cancelIncomeModal");
    btnCancelIncomeModal.onclick = function() {
        incomeModal.style.display = "none";
    };

    btnAddIncome = document.getElementById("addIncome");
    btnAddIncome.onclick = function() {
        addIncome();
    };

    // declare income type selector
    incomeTypeSelector = document.getElementById("incomeType");
}

function openIncomeModal(modalDetails) {
    btnAddIncome.value = modalDetails.button;
    incomeModal.style.display = "block";

    btnAddIncome.onclick = function() {
        updateIncomeWithCallback(modalDetails);
    };
}

function closeIncomeModal() {
    incomeModal.style.display = "none";
}

function updateIncomeWithCallback(callback) {
    var request = getXmlHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                // close modal
                closeIncomeModal();

                setIncome(JSON.parse(request.responseText));

                showIncome(JSON.parse(request.responseText).income);

                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len === 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };

    var token = userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));
    var userId = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));


    let incomeType = incomeTypeSelector.options[incomeTypeSelector.selectedIndex].value;
    incomeType = incomeFields.INCOME_TYPE + "=" + escape(incomeType);
    var incomeValue = incomeFields.AMOUNT + "=" + escape(document.getElementById("incomeValue").value);
    
    let incomeDate = document.getElementById("incomeDate").value;
    
    let iDate = "date=" + formatDate(new Date(incomeDate));

    var data = userId + "&" + incomeType + "&" + incomeValue + "&" + iDate + "&" + token;

    request.open("POST", ctx + "/dashboard/user-controller/add-income", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

function updateIncome(income) {
    var request = getXmlHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                // close modal
                closeIncomeModal();

//                getUserDetails();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };

    var token = escape(window.localStorage.getItem(userFields.TOKEN));
    var userId = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));
    
    let date = new Date().addMonths(income.scheduled_for, 1);
    income.amount = document.getElementById("incomeValue").value;
    income.scheduled_for = formatDate(date);

    let incomeDto = {
        income: income
    };
    
    var data = JSON.stringify(incomeDto);

    request.open("PUT", ctx + "/api/income/update-income?" + userId, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.APPLICATION_JSON);
    request.setRequestHeader(requestHeader.AUTHORIZATION, "Bearer " + token);
    request.send(data);
}

function activateIncomeTimer(income, lastIncome) {
    
    (function(income, scheduledIncome) {
        let today = new Date();
        let timeScheduled = new Date(income.scheduled_for);

        let timeDiff = today.getMonth() - timeScheduled.getMonth();
        let isTimeReached =  timeDiff === 1;
        if (isTimeReached && scheduledIncome !== null) {
            if (!scheduledIncome.on_income_changed_status) {
                addScheduledIncome(income, scheduledIncome);
                return;
            }
        }

        window.setTimeout(arguments.callee, 1000, income, scheduledIncome);
    })(income, lastIncome);
}

/**
 * add income to database
 * @param {Income} income add income to database
 */
function addScheduledIncome(income, lastIncome) {
    let incomeTypes = document.getElementById("incomeType").options;
    
    let count = 0;
    for (let i = 0; i < incomeTypes.length; i++) {
        if (incomeType[i].innerHTML === income.income_type) {
            document.getElementById("incomeType").options[count].selected = true;
            break;
        }
        count++;
    }
    
    if (lastIncome.amount === 0) {
        document.getElementById("incomeValue").value = income.amount;
    } else {
        document.getElementById("incomeValue").value = lastIncome.amount;
    }
    let newDate = formatDate(new Date().addMonths(income.scheduled_for, 1));
    document.getElementById("incomeDate").value = newDate;
    document.getElementById("incomeDate").disabled = true;
    document.getElementById("incomeModalTitle").innerHTML = "Confirm Payments";
    
    btnAddIncome.value = "Confirm";
    incomeModal.style.display = "block";

    btnAddIncome.onclick = function() {
        updateIncome(income);
    };
}

function addIncome() {
    closeOptionsMenu();

    var request = getXmlHttpRequest();
    request.onreadystatechanged = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                // close modal
                closeIncomeModal();
                
                setIncome(JSON.parse(request.responseText));

                showIncome(JSON.parse(request.responseText).income);
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }

        }
    };

    var token = userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));
    var userId = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));

//    var incomeDescription = incomeFields.ACCOUNT_TYPE + "=" + escape(document.getElementById("incomeDescription").value);
    var incomeType = incomeTypeSelector.options[incomeTypeSelector.selectedIndex].value;
    incomeType = incomeFields.INCOME_TYPE + "=" + escape(incomeType);
    var value = incomeFields.AMOUNT + "=" + escape(document.getElementById("incomeValue").value);

    let incomeDate = document.getElementById("incomeDate").value;
    
    let iDate = "date=" + formatDate(new Date(incomeDate));
    
    var data = userId + "&" + incomeType + "&" + value + "&" + iDate + "&" + token;

    request.open("POST", ctx + "/dashboard/user-controller/add-income", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

/**
 * display income in the dropdown option menu if available
 * 
 * @param {Income} income Contain income details
 */
function showIncome(income, lastIncome) {
    if (income !== undefined) {
        if (income.amount > 0) {
            incomeSpan.hidden = false;
            incomeSpan.textContent = incomeSpan.innerHTML.split(":")[0] + ": " + income.amount;
            
            btnOpenIncomeModal.hidden = true;
            
            activateIncomeTimer(income, lastIncome);
        }
    }
}