var btnOpenEnvelopeModal;
var cancelEnvelopeModal;
var btnSubmitEnvelope;

var categorySelector;
let scheduleSelector;
let daySelector;
let timePicker;
let datePicker;

let dayOfWeek;

let envelopeDto;
let envelopeList = {}
let envelopeTbody;

let groceryDto = {
    groceries: []
}

const categoryOption = {
    GROCERIES: 'Groceries',
    EXPENSES: 'Expenses'
}

const scheduleType = {
    SCHEDULED: 'Scheduled',
    DAILY: 'Daily',
    WEEKLY: 'Weekly',
    MONTHLY: 'Monthly'
}

/**
 * Envelope Fields
 */
let selectedEnvelopeType;

function configureEnvelope() {
    // Initialize the table
    envelopeTbody = document.getElementById("envelopeItems").getElementsByTagName("tbody")[0];
    
    btnOpenEnvelopeModal = document.getElementById("btnOpenEnvelopeModal");
    btnOpenEnvelopeModal.onclick = function() {
        envelopeModal.style.display = "block";
    }

    // closing modal
    cancelEnvelopeModal = document.getElementById("cancelEnvelopeModal");
    cancelEnvelopeModal.onclick = function() {
        envelopeModal.style.display = "none";
    }

    btnSubmitEnvelope = document.getElementById("btnSaveEnvelope");
    btnSubmitEnvelope.onclick = function() {
        sendEnvelopeRequest();
    }

    // Grocery section
    let grocerySection = document.getElementById("groceries-envelopeDto");
    // Envelope sections
    let envelopeSection = document.getElementById("expense-envelopeDto");

    categorySelector = document.getElementById("categorySelector");
    categorySelector.addEventListener("change", function () {
        selectedEnvelopeType = categorySelector.options[categorySelector.selectedIndex].value;
        if (selectedEnvelopeType == categoryOption.GROCERIES) {
            grocerySection.hidden = false;
            envelopeSection.hidden = true;
        } else if (selectedEnvelopeType == categoryOption.EXPENSES) {
            grocerySection.hidden = true;
            envelopeSection.hidden = false;
        }
    });

    // Define event for time picker

    dayOfWeek = document.getElementById("dayOfWeek");
    timePicker = document.getElementById("timePicker");
    datePicker = document.getElementById("datePicker");

    scheduleSelector = document.getElementById("scheduleSelector");
    scheduleSelector.addEventListener("change", function () {
        let selected = scheduleSelector.options[scheduleSelector.selectedIndex].value;
        if (selected == scheduleType.WEEKLY) {
            // weekly notification
            dayOfWeek.hidden = false;
            timePicker.hidden = false;
            datePicker.hidden = true;
        } else if (selected == scheduleType.SCHEDULED) {
            // scheduled notification - happens only once when scheduled
            dayOfWeek.hidden = true;
            timePicker.hidden = false;
            datePicker.hidden = false;
        } else if (selected == scheduleType.MONTHLY) {
            // daily notification to take care of expenses
            dayOfWeek.hidden = true;
            timePicker.hidden = false;
            datePicker.hidden = false;
        } else {
            // daily notification to take care of expenses
            dayOfWeek.hidden = true;
            timePicker.hidden = false;
            datePicker.hidden = true;
        }
    });

    daySelector = document.getElementById("daySelector");

    getAllEnvelopes();
    // TODO: on envelope list change update table.
}

function getAllEnvelopes() {
    let request = getXmlHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState == 4) {
            if (request.status == 200) {
                let json = request.responseText;
                let envelopeDtoList = JSON.parse(json);

                // update the global list; list is empty
                for (let i = 0; i < envelopeDtoList.length; i++) {
                    envelopeDto = envelopeDtoList[i];
                    let envelope = envelopeDto.envelope;
                    envelopeList[envelope.envelope_id] = envelopeDto;
                }

                setEnvelopes("");
            }
        }
    }

    let data = userFields.TOKEN + "=" + escape(window.localStorage.getItem("token"));
    data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));

    request.open("GET", ctx + "/dashboard/envelopes-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

function openEvelopeModal(callback) {
    envelopeModal.style.display = "block";

    btnSubmitEnvelope.onclick = function() {
        updateEnvelope(callback);
    }
}

/**
 * sends a request throught the controller to add an item to grocery
 */
function sendEnvelopeRequest() {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                envelopeDto = JSON.parse(json);
                addEnvelopeToList(envelopeDto);
            } else {
                showError();
            }
        }
    }

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/envelopes-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.setRequestHeader(requestHeader.AUTHORIZATION, token);
    request.send();
}

function updateEnvelope(callback) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                envelopeDto = JSON.parse(json);

                addEnvelopeToList(envelopeDto);

                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len == 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
            } else {
                showError();
            }
        }
    }

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/envelopes-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

/**
 * Add the item added by the user to aa global list variable
 * @param {Envelopes[]} envelopeDto contains a list of envelopes with elements
 */
function addEnvelopeToList(envelopeDto) {
    let envelope = envelopeDto.envelope;
    // check size before adding item
    let beforeLen = Object.keys(envelopeList).length;

    envelopeList[envelope.envelope_id] = envelopeDto;

    // check length after adding item
    let afterLen = Object.keys(envelopeList).length;

    if (afterLen > beforeLen) {
        setEnvelopes(envelope.envelope_id);
    }

}

/**
 * Update the ui with data from server about envelopes
 * @param {Envelope[]} envelopes all the envelopes from the server for this household.
 * @param {String} envelopeId id that identifies that added envelope
 */
function setEnvelopes(envelopeId) {
    // check that the table is not empty
    let rows = envelopeTbody.rows;
    let envelopes = Object.keys(envelopeList);
    if (rows.length == 0) {
        // if empty, check that envelope list is empty
        if (envelopes.length > 0) {
            // if envelope list is not empty add items to table
            for (let i = 0; i < envelopes.length; i++) {
                let envelope = envelopes[i];
                updateEnvelopeTable(envelope, i);
            }
        }
    } else {
        // if table is not empty, check that the item does not exist
        if (envelopes.length > 0) {
            let envelope = envelopeList[envelopeId];
            updateEnvelopeTable(envelope);
        }
    }
    // update the table with new item
}

/**
 * update the envelope table with the name of the envelope and scheduled date
 * @param {Envelope} envelope contains all items of the envelope
 * @param {Integer} index represents the row to update
 */
function updateEnvelopeTable(envelope, index) {
    let row = envelopeTbody.insertRow(index);

    let nameCell = row.insertCell(0);
    nameCell.innerHTML = envelope.envelope_name;
    let dateCell = row.insertCell(1);
    dateCell.innerHTML = envelope.scheduled_for;
    let typeCell = row.insertCell(2);
    typeCell.innerHTML = envelope.scheduled_type;
}

/**
 * Get input data from the user and serialize
 */
function serializeData() {
    var userId = escape(window.localStorage.getItem("user_id"));
    var token = escape(window.localStorage.getItem("token"));
    var username = escape(window.localStorage.getItem("username"));

    // get elements in
    let envelopeName = document.getElementById("envelopeName");
    let amount = document.getElementById("totalAmount");
    let time = document.getElementById("scheduledHour");
    let date = document.getElementById("scheduledDate");
    let liabilitiesObj;

    if (envelopeName.value == "") {
        var envelopeModalError = document.getElementById("envelopeModalError");
        envelopeModalError.innerHTML = "This field is required: Envelope Name";
        return;
    }
    
    // get list depending on category
    let items;
    if (selectedEnvelopeType == categoryOption.EXPENSES) {
        liabilitiesObj = getLiabilities();
        items = JSON.stringify(liabilitiesObj);
    } else {
        groceryDto.groceries = Object.values(groceryListObj);
        items = JSON.stringify(groceryDto);
    }

    if (selectedEnvelopeType == null) {
        selectedEnvelopeType = categorySelector.options[categorySelector.selectedIndex].value;
    }

    if (items == "" && selectedEnvelopeType == categoryOption.GROCERIES) {
        var envelopeModalError = document.getElementById("envelopeModalError");
        envelopeModalError.innerHTML = "Your grocery list is empty";
        return;
    }

    let data = userFields.TOKEN + "=" + token + "&";
    data += userFields.USER_ID + "=" + userId + "&";
    data += envelopeFields.ENVELOPE_NAME + "=" + escape(envelopeName.value) + "&";
    data += envelopeFields.CATEGORY + "=" + escape(selectedEnvelopeType) + "&";
    data += envelopeFields.TOTAL_AMOUNT + "=" + escape(amount.innerHTML) + "&";
    
    // get date
    let dateTime;
    let scheduledType = "";
    let selected = scheduleSelector.options[scheduleSelector.selectedIndex].value;
    if (selected == scheduleType.WEEKLY) {
        dateTime = daySelector.options[daySelector.selectedIndex].value;
        dateTime += " " + time.value;
        scheduledType = scheduleType.WEEKLY;
    } else if (selected == scheduleType.SCHEDULED) {
        dateTime = date.value == "" ? "" : date.value;
        dateTime += " " + (time.value == "" ? "" : time.value);
        scheduledType = scheduleType.SCHEDULED
    } else if (selected == scheduleType.MONTHLY) {
        dateTime = date.value == "" ? "" : date.value;
        dateTime += " " + (time.value == "" ? "" : time.value);
        scheduledType = scheduleType.MONTHLY
    } else {
        dateTime = time.value;
        scheduledType = scheduleType.DAILY
    }

    data += envelopeFields.SCHEDULE + "=" + escape(dateTime) + "&";
    data += envelopeFields.SCHEDULED_TYPE + "=" + escape(scheduledType) + "&";
    data += envelopeFields.LIABILITIES + "=" + escape(items);

    return data;
}

function getLiabilities() {
    let expenseName = document.getElementById("expenseName").innerHTML;
    let expenseDesc = document.getElementById("expenseDesc").innerHTML;
    let expenseAmount = document.getElementById("expenseAmount").innerHTML;
    let expensePayeeName = document.getElementById("expensePayeeName").innerHTML;
    let businessNumber = document.getElementById("expenseBusinessNumber").innerHTML;
    let phoneNumber = document.getElementById("phoneNumber").innerHTML;

    if (expenseName == "" || expenseDesc == "" || expenseAmount == "" || expensePayeeName == "") {
        var envelopeModalError = document.getElementById("envelopeModalError");
        envelopeModalError.innerHTML = "The liability section requires: Name, Description, Amount and Payee Name";
        return;
    }

    expense.name = expenseName;
    expense.desc = expenseDesc;
    expense.amount = expenseAmount;
    expense.payee = expensePayeeName;
    expense.business_number = businessNumber;
    expense.phone_number = phoneNumber;

    return expense;
}

// function serializeData() {
//     var userId = "user_id=" + escape(window.localStorage.getItem("user_id"));
//     var token = "token=" + escape(window.localStorage.getItem("token"));
//     var username = "username=" + escape(window.localStorage.getItem("username"));

//     // serialize grocery items details
//     var id = grocerySelected == null ? "" : grocerySelected.grocery_id;
//     var name = groceryName.value;
//     var desc = groceryDesc.value;
//     var price = groceryPrice.value;
//     var required = groceryRequired.value;
//     var remaining = groceryRemaining.value;

//     if (name == "" || desc == "" || price == "" || required == "" || remaining == "") {

//         var error = document.getElementById("envelopeModalError");
//         error.innerHTML = "All fields are required!";
//         error.hidden = false;
//         return;
//     }

//     id = "grocery_id=" + escape(id);
//     remaining = "remaining_quantity=" + escape(remaining);
//     required = "required_quantity=" + escape(required);
//     price = "grocery_price=" + escape(price);
//     desc = "grocery_description=" + escape(desc);
//     name = "grocery_name=" + escape(name);

//     var data = userId + "&";
//     data += token + "&";
//     data += username + "&";
//     data += name + "&";
//     data += desc + "&";
//     data += price + "&";
//     data += required + "&";
//     data += remaining + "&";
//     data += id;

//     return data;
// }

/**
 * Update the envelopeDto's total amount
 * @param {double} amount total amount of comodity
 */
function onRowLoaded(amount) {
    var totalAmount = document.getElementById("totalAmount");

    var currentTotal = totalAmount.innerHTML;
    currentTotal = parseInt(amount) + parseInt(currentTotal);

    totalAmount.innerHTML = currentTotal;
}

function updateTotalAmount(previous, newPrice) {
    var totalAmount = document.getElementById("totalAmount");

    var currentTotal = totalAmount.innerHTML;
    currentTotal = parseInt(currentTotal) - parseInt(previous);
    currentTotal += parseInt(newPrice);

    totalAmount.innerHTML = currentTotal;

}