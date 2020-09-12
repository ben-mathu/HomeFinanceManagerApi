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
                reportModal.style.display = "block";
            }
        }
    };
    
    let userId = escape(window.localStorage.getItem(userFields.USER_ID));
    let token = escape(window.localStorage.getItem(userFields.TOKEN));
    
    if (fldDateFrom.value === "" || fldDateTo.value === '') {
        showMessage("The date range is required.");
        return;
    }
    
    let fromDate = "from" + "=" + escape(fldDateFrom.value);
    let toDate = "to=" + escape(fldDateTo.value);
    
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

