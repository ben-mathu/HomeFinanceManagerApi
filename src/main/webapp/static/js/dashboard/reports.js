let btnShowReports;
let fldDateFrom;
let fldDateTo;

function configureReport() {
    reportModal = document.getElementById("reportModal");
    fldDateFrom = document.getElementById("dateFrom");
    fldDateTo = document.getElementById("dateTo");
    
    btnShowReports = document.getElementById("btnReport");
    btnShowReports.addEventListener("click", function(event) {
        openReportModal();
    });
}

function openReportModal() {
    
    let request = getXmlHttpRequest();
    
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            if (request.status === 200) {
                let obj = JSON.parse(request.responseText);
                let numMonths = document.getElementById("numMonths");
                
                // add number of months
                numMonths.textContent = obj.months_difference + " month(s) ending:";
                
                // add "to" date
                let reportdate = document.getElementById("reportdate");
                reportdate.textContent = obj.date_ending;
                
//                Add year
                let reportYear = document.getElementById("reportYear");
                reportYear.textContent = obj.years_range;
                
                let reportIncome = document.getElementById("reportIncome");
                reportIncome.textContent = obj.income;
                
                let incomeTotal = document.getElementById("incomeTotal");
                incomeTotal.textContent = obj.income;
                
                let expenseBody = document.getElementById("expenseBody");
                
                let expenseList = obj.expenses;
                expenseList.forEach(expense => {
                    let tr = document.createElement("tr");
                    let td = document.createElement("td");
                    td.textContent = expense.expense_type;
                    td.classList.add("td-report-left");
                    tr.appendChild(td);
                    
                    let tdAmount = document.createElement("td");
                    tdAmount.textContent = expense.amount;
                    tdAmount.classList.add("td-report-left");
                    tr.appendChild(tdAmount);
                    expenseBody.appendChild(tr);
                });
                
                let trTotal = document.createElement("tr");
                
                let tdTitle = document.createElement("td");
                tdTitle.textContent = "Total Cost";
                tdTitle.classList.add("td-report-left");
                trTotal.appendChild(tdTitle);
                
                let tdValue = document.createElement("td");
                tdValue.textContent = obj.expense_total_amount;
                tdValue.classList.add("td-report-left");

                trTotal.appendChild(tdValue);
                expenseBody.appendChild(trTotal);
                
                let incomeBeforeTax = document.getElementById("incomeBeforeTax");
                incomeBeforeTax.textContent = obj.income;
                
                let incomeTax = document.getElementById("incomeTax");
                incomeTax.textContent = obj.tax;
                
                let personalRelief = document.getElementById("personalRelief");
                personalRelief.textContent = obj.personal_relief;
                
                let incomeAfterTax = document.getElementById("incomeAfterTax");
                incomeAfterTax.textContent = obj.income_after_tax;
                
                let netIncome = document.getElementById("netIncome");
                netIncome.textContent = obj.net_income;
                
                reportModal.style.display = "block";
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };
    
    let userId = escape(window.localStorage.getItem(userFields.USER_ID));
    let token = escape(window.localStorage.getItem(userFields.TOKEN));
    
    if (fldDateFrom.value === "" || fldDateTo.value === '') {
        showMessage("The date range is required.");
        return;
    }
    
    let fromDate = "from" + "=" + escape(formatDate(new Date(fldDateFrom.value)) + " 00:00");
    let toDate = "to=" + escape(formatDate(new Date(fldDateTo.value)) + " " + formatTime(new Date()));
    
    let data = fromDate + "&" + toDate + "&" + userFields.USER_ID + "=" + userId + "&" + userFields.TOKEN + "=" + token;
    
    request.open("GET", ctx + "/reports/reports-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

/**
 * Shows messages with reference on dashboard activities
 * @param {type} message string to be displayed at the bottom of the screen
 */
function showMessage(message) {
    let messageDialog = document.getElementById("messageDialog");
    let messageReport = document.getElementById("messageReport");
    
    messageDialog.style.display = "block";
    messageReport.textContent = message;
    
    window.setTimeout(function() {
        messageDialog.style.display = "none";
    }, 5000);
}

