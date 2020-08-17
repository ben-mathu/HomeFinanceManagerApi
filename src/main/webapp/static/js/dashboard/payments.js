// Dialog elements
let moneyJarId;

// Input fields
let paymentAmount;

// Buttons
let btnEditDetails;
let btnPay;

let paymentExpenseSection;
let paymentGorcerySection;

let btnExpandLiabilities;
let liabilitySection;
let isExpanded = false;

let paymentTemplate;
let notificationTemplate;

const paybillFields = {
    SHORT_CODE: "BusinessShortCode",
    AMOUNT: "Amount",
    PARTY_A: "PartyA",
    PARTY_B: "PartyB",
    PHONE_NUMBER: "PhoneNumber",
    ACCOUNT_REF: "AccountReference",
    TRANSACTION_DESC: "TransactionDesc"
}

/**
 * Configure payment elements and global variables
 */
function configurePayments() {
    paymentTemplate = document.getElementById("paymentTemplate").content;
    notificationTemplate = document.getElementById("notificationDetails").content;

    notifications.registerLister(function(val, clone) {
        window.localStorage.setItem(val, JSON.stringify(notifications.getAll));

        let notificationContainer = document.getElementById("notificationContainer");
        notificationContainer.appendChild(clone);

        notificationCount++;
    });
}

/**
 * Display the notification/message bubble for due expenses/bills
 * 
 * @param {String} jarId key, represents element in moneyJarList
 * @param {JarDto} jarDto class that encapsulates the jar items/elements
 */
let count = 0;
function showNotificationDialog(jarId, jarDto) {
    let jar = jarDto.jar;

    let templateClone = paymentTemplate.cloneNode(true);

    templateClone.querySelector("#moneyJarId").id += count;
    moneyJarId = templateClone.querySelector("#moneyJarId" + count);
    moneyJarId.value = jarId;

    templateClone.querySelector("#liabilitySection").id += count;
    liabilitySection = templateClone.querySelector("#liabilitySection" + count);

    if (jar.category == "Expenses") {
        let expenseItems = jarDto.expense;

        expense.expense_id = expenseItems.expense_id;
        expense.expense_name = expenseItems.expense_name;
        expense.expense_description = expenseItems.expense_description;
        expense.amount = expenseItems.amount;
        expense.payee_name = expenseItems.payee_name;
        expense.type = expenseItems.type
        expense.business_number = expenseItems.business_number;
        expense.account_number = expenseItems.account_number;

        templateClone.querySelector("#paymentExpense").id += count;
        paymentExpenseSection = templateClone.querySelector("#paymentExpense" + count);
        paymentExpenseSection.appendChild(setExpense(jarId));
        liabilitySection.appendChild(paymentExpenseSection);
    } else {
        let groceries = jarDto.groceries;

        templateClone.querySelector("#paymentGrocery").id += count;
        paymentGorcerySection = templateClone.querySelector("#paymentGrocery" + count);
        paymentGorcerySection.appendChild(setGroceries(groceries));
        liabilitySection.appendChild(paymentGorcerySection);
    }

    let nameTitle = jar.jar_label;
    templateClone.querySelector("#nameTitle").id += count;
    let nameTitleSpan = templateClone.querySelector("#nameTitle" + count);
    nameTitleSpan.innerHTML = nameTitle;
    
    templateClone.querySelector("#paymentCategorySpan").id += count;
    let categoryType = templateClone.querySelector("#paymentCategorySpan"+count);
    categoryType.innerHTML = jar.category;

    templateClone.querySelector("#paymentAmount").id += count;
    paymentAmount = templateClone.querySelector("#paymentAmount" + count);
    paymentAmount.innerHTML = jar.amount;

    templateClone.querySelector("#btnEditDetails").id += count;
    btnEditDetails = templateClone.querySelector("#btnEditDetails" + count);
    btnEditDetails.onclick = function () {
        openJarModalForEdit(moneyJarId.value);
    }

    templateClone.querySelector("#btnPay").id += count;
    btnPay = templateClone.querySelector("#btnPay" + count);
    btnPay.onclick = function() {
        makePayments(moneyJarId.value);
    }

    templateClone.querySelector("#expandButton").id += count;
    btnExpandLiabilities = templateClone.querySelector("#expandButton" + count);
    btnExpandLiabilities.onclick = function () {
        if (isExpanded) {
            liabilitySection.hidden = true;
            isExpanded = false;
        } else {
            liabilitySection.hidden = false;
            isExpanded = true;
        }
    }

    templateClone.querySelector("#paymentDetailsContainer").id += count;
    let paymentDialog = templateClone.querySelector("#paymentDetailsContainer" + count);

    window.setTimeout(function() {
        paymentDialog.style.display = "none";

        addNotification(jarId);
    }, 10000);

    let paymentDetails = document.getElementById("paymentDialogContainer");
    paymentDetails.appendChild(templateClone);

    let paymentDueDialog = document.getElementById("paymentDueDialog");
    paymentDueDialog.style.display = "block";

    count++;
}

let notifications = {
    notificationList: {},
    notificationAddListener: function(val) {},
    setNotification: function(id, val, clone) {
        this.notificationList[id] = val;
        this.notificationAddListener(id, clone);
    },
    getNotification: function(val) {
        return this.notificationList[val];
    },
    getAll: function() {
        return this.notificationList;
    },
    registerLister: function(listener) {
        this.notificationAddListener = listener;
    }
}

/**
 * add to a list of notifications
 * 
 * @param {string} jarId references a jar
 */
let notificationCount = 0;
function addNotification(jarId) {
    let jarDto = jars.getJar(jarId);
    
    // create a unique id for notification
    let dateNow = Date.now();

    let jar = jarDto.jar;

    let templateClone = notificationTemplate.cloneNode(true);

    templateClone.querySelector("#notificationTitle").id += notificationCount;
    let notificationTitle = templateClone.querySelector("#notificationTitle" + notificationCount);
    notificationTitle.innerHTML = jar.jar_label;

    templateClone.querySelector("#notificationMessage").id += notificationCount;
    let notificationMessage = templateClone.querySelector("#notificationMessage" + notificationCount);
    notificationMessage.innerHTML = jar.category + " Amount: " + jar.amount;
    
    jarId += dateNow;
    notifications.setNotification(jarId, jarDto, templateClone);
}

/**
 * Send a transaction to Mpesa
 * 
 * @param {String} jarId represents a money jar element in the moneyJarList field
 */
function makePayments(jarId) {

    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                let responseData = request.responseText
                showSuccessNotification(responseData, jarId);
            }
        }
    }

    let data = serializePaymentData(jarId);

    request.open("POST", ctx + "/dashboard/transactions/send-payment" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

function showSuccessNotification(data, notificationId) {
    let report = JSON.parse(data);

    let request = getXmlHttpRequest();

    request.onload = function() {
        if (request.status == 200) {
            updateNoitification(notificationId);
        }
    }

    request.open("POST", ctx + "/mpesa/lnmo-url/" + report.subject);
}

/**
 * Updates the notification depending on the response got for payment
 * 
 * @param {string} notificationId identify notification of payement request
 */
function updateNoitification(notificationId) {
    
}

function serializePaymentData(jarId) {
    let jarElement = jars.getJar(jarId);
    let jar = jarElement.jar;
    
    let data = "";

    let token = window.localStorage.getItem(userFields.TOKEN)
    data += userFields.TOKEN + "=" + token + "&";
    
    if (jar.category == categoryOption.EXPENSE) {
        let expenseItem = jar.expense;
        data += paybillFields.SHORT_CODE + "=" + expenseItem.business_number + "&";
        data += paybillFields.AMOUNT + "=" + jar.amount + "&";
        data += paybillFields.PARTY_A + "=" + expenseItem.account_number + "&";
        data += paybillFields.PARTY_B + "=" + expenseItem.business_number + "&";
        data += paybillFields.PHONE_NUMBER + "=" + expenseItem.account_number + "&";
        data += paybillFields.ACCOUNT_REF + "=account" + "&";
        data += paybillFields.TRANSACTION_DESC + "=First transaction from code";
    }
    return data;
}