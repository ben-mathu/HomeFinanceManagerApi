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
};

const topUpFields = {
    SHORT_CODE: "BusinessShortCode",
    AMOUNT: "Amount",
    PARTY_A: "PartyA",
    PARTY_B: "PartyB",
    PHONE_NUMBER: "PhoneNumber",
    ACCOUNT_REF: "AccountReference",
    TRANSACTION_DESC: "TransactionDesc"
};

/**
 * make payments array functions
 */
let makePaymentsArrays = {};

/**
 * Configure payment elements and global variables
 */
function configurePayments() {
    paymentTemplate = document.getElementById("paymentTemplate").content;
    notificationTemplate = document.getElementById("notificationDetails").content;

    notifications.registerListener(function(val, clone) {

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

    if (jar.category === categoryOption.SINGLE) {
        let expenseItems = jarDto.expense;

        templateClone.querySelector("#paymentExpense").id += count;
        paymentExpenseSection = templateClone.querySelector("#paymentExpense" + count);
        paymentExpenseSection.appendChild(setExpense(jarId, expenseItems));
        liabilitySection.appendChild(paymentExpenseSection);
    } else {
        let groceries = jarDto.groceries;

        templateClone.querySelector("#paymentGrocery").id += count;
        paymentGorcerySection = templateClone.querySelector("#paymentGrocery" + count);
        paymentGorcerySection.appendChild(setGroceries(groceries));
        liabilitySection.appendChild(paymentGorcerySection);
    }

    let nameTitle = jar.expense_type;
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
    };

    templateClone.querySelector("#btnPay").id += count;
    btnPay = templateClone.querySelector("#btnPay" + count);
    btnPay.addEventListener("click", function(event) {
        let elementIndex = event.target.id[event.target.id.length - 1];
        let mJarId = document.getElementById("moneyJarId" + elementIndex);
        makePayments(mJarId.value);
    }, false);

    templateClone.querySelector("#expandButton").id += count;
    btnExpandLiabilities = templateClone.querySelector("#expandButton" + count);
    btnExpandLiabilities.onclick = function (event) {
        let elementIndex = event.target.id[event.target.id.length - 1];
        liabilitySection = document.getElementById("liabilitySection" + elementIndex);
        
        if (isExpanded) {
            liabilitySection.hidden = true;
            isExpanded = false;
        } else {
            liabilitySection.hidden = false;
            isExpanded = true;
        }
    };
    
    templateClone.querySelector("#paymentDetailsContainer").id += count;
    let paymentDialog = templateClone.querySelector("#paymentDetailsContainer" + count);

    window.setTimeout(function() {
        paymentDialog.style.display = "none";

        addNotification(jarId);
    }, 20000);

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
    registerListener: function(listener) {
        this.notificationAddListener = listener;
    }
};

/**
 * add to a list of notifications
 * 
 * @param {string} jarId references a jar
 */
let notificationCount = 0;
function addNotification(jarId) {
    let jarDto = jars.getJar(jarId);
    let jar = jarDto.jar;
    
    populateNotificationSection(jarId);
    
    // update date depending on schedule
    let date;
    if (jar.scheduled_type === scheduleType.DAILY) {
        date = new Date().addDays()(jar.scheduled_for, 1);
    } else if (jar.scheduled_type === scheduleType.WEEKLY) {
        date = new Date().addDays(jar.scheduled_for, 7);
    } else if (jar.scheduled_type === scheduleType.MONTHLY) {
        date = new Date().addMonths(jar.scheduled_for, 1);
    } else if (jar.scheduled_type === scheduleType.SCHEDULED) {
        date = new Date(jar.scheduled_for);
    }
    
//    format date
    if (date !== undefined) {
        const dateTimeFormat = new Intl.DateTimeFormat('en', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', hour12: false});
        const [{ value: month },,{ value: day },,{ value: year },,{value: hour},,{value: minute}] = dateTimeFormat .formatToParts(date );
        
        let dateStr = `${year}-${month}-${day} ${hour}:${minute}`;
        sendJarRequestJson(jarDto, dateStr);
    }
    
    jar.jar_status = true;
    jarDto.jar = jar;
    jars.setJar(jarId, jarDto);
    updateMoneyJarJson(jarId);
}

/**
 * update the notification section
 * @param {string} jarId identifies a jar object
 */
function populateNotificationSection(jarId) {
    let jarDto = jars.getJar(jarId);
    
    // create a unique id for notification
    let dateNow = Date.now();

    let jar = jarDto.jar;

    let templateClone = notificationTemplate.cloneNode(true);
    
    templateClone.querySelector("#notificationId").id += notificationCount;
    let notificationId = templateClone.querySelector("#notificationId" + notificationCount);
    notificationId.value = jarId;

    templateClone.querySelector("#notificationTitle").id += notificationCount;
    let notificationTitle = templateClone.querySelector("#notificationTitle" + notificationCount);
    notificationTitle.innerHTML = jar.expense_type;

    templateClone.querySelector("#notificationMessage").id += notificationCount;
    let notificationMessage = templateClone.querySelector("#notificationMessage" + notificationCount);
    notificationMessage.innerHTML = jar.category + " Amount: " + jar.amount;
    
    templateClone.querySelector("#notificationDetailsCon").id += notificationCount;
    let notificationItem = templateClone.querySelector("#notificationDetailsCon" + notificationCount);
    notificationItem.addEventListener("click", function (event) {
        let itemIndex = event.target.id[event.target.id.length - 1];
        let id = document.getElementById("notificationId" + itemIndex);
        openJarModalForEdit(id.value);
    });
    
    let id = jarId + "-" + dateNow;
    notifications.setNotification(id, jarDto, templateClone);
}

Date.prototype.addHours = function(scheduled, hours) {
    let date = new Date(scheduled);
    date.setHours(date.getHours() + hours);
    return date;
};

Date.prototype.addDays = function(scheduled, days) {
    let date = new Date(scheduled);
    date.setDate(date.getDate() + days);
    return date;
};

Date.prototype.addWeeks = function(scheduled, weeks) {
    let date = new Date(scheduled);
    date.setDate(date.getDate() + weeks * 7);
    return date;
};

Date.prototype.addMonths = function(scheduled, months) {
    let date = new Date(scheduled);
    date.setMonth(date.getMonth() + months);
    return date;
};

/**
 * Send a transaction to Mpesa
 * 
 * @param {String} jarId represents a money jar element in the moneyJarList field
 */
function makePayments(jarId) {
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                let responseData = request.responseText;
                showSuccessNotification(responseData, jarId);
            }
        }
    };

    let data = serializePaymentData(jarId);

    request.open("POST", ctx + "/dashboard/transactions/send-payment" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

function showSuccessNotification(data, notificationId) {
    let report = JSON.parse(data);

    let request = getXmlHttpRequest();

    request.onload = function() {
        if (request.status === 200) {
            updateNoitification(notificationId);
        }
    };

    request.open("POST", ctx + "/mpesa/lnmo-url/" + report.subject);
}

/**
 * Updates the notification depending on the response got for payment
 * 
 * @param {string} notificationId identify notification of payement request
 */
function updateNoitification(notificationId) {
    let paymentStatus = paymentTemplate.querySelector("#paymentStatus");
    paymentStatus.innerHTML = "paid";
    paymentStatus.style.color = "white";

    let notification = notifications.getNotification(notificationId);
}

function serializePaymentData(jarId) {
    let jarElement = jars.getJar(jarId);
    let jar = jarElement.jar;
    
    let data = "";

    let token = window.localStorage.getItem(userFields.TOKEN);
    data += userFields.TOKEN + "=" + token + "&";

    let isSameCategory = isSameCategoryFun(jar.category);
    
    if (isSameCategory) {
        let expenseItem = jarElement.expense;
        data += paybillFields.SHORT_CODE + "=" + escape(expenseItem.business_number) + "&";
        data += paybillFields.AMOUNT + "=" + escape(jar.amount) + "&";
        data += paybillFields.PARTY_A + "=" + escape(expenseItem.account_number) + "&";
        data += paybillFields.PARTY_B + "=" + escape(expenseItem.business_number) + "&";
        data += paybillFields.PHONE_NUMBER + "=" + escape(expenseItem.account_number) + "&";
        data += paybillFields.ACCOUNT_REF + "=" + escape("account") + "&";
        data += paybillFields.TRANSACTION_DESC + "=" + escape("First transaction from code") + "&";
        data += jarFields.JAR_ID + "=" + escape(jarId);
    }
    return data;
}

function isSameCategoryFun(category) {
    return category === categoryOption.EXPENSES;
}