var btnOpenJarModal;
var cancelJarModal;
var btnSubmitJar;

var categorySelector;
let scheduleSelector;
let daySelector;
let timePicker;
let datePicker;

let expenseSection;
let grocerySection;
let expenseItemsSection;

let dayOfWeek;

let errorMessage;

const expenseTypes = {
    'Rent': 'Fixed',
    'Taxes': 'Fixed',
    'Loan': 'Fixed',
    'Insurance': 'Fixed',
    'Electricity': 'Saving',
    'Water': 'Saving',
    'Groceries': 'Saving',
    'Health': 'Saving',
    'Personal': 'Saving',
    'Entertainment': 'Saving',
    'Vehicle Maintenance': 'Saving',
    'Employee': 'Fixed'
};

let jars = {
    moneyJarList: {},
    moneyJarListListener: function(val) {},
    setJar: function(id, val) {
        this.moneyJarList[id] = val;
        this.moneyJarListListener(val);
    },
    deleteJar: function(id) {
        delete this.moneyJarList[id];
    },
    getJar: function(val) {
        return this.moneyJarList[val];
    },
    getJarList: function() {
        return this.moneyJarList;
    },
    removeAll: function() {
        this.moneyJarList = {};
    },
    registerListener: function(listener) {
        this.moneyJarListListener = listener;
    }
};

// templating
let jarTemplate;
let jarList;

let expenseListDto = {
    groceries: []
};

const categoryOption = {
    LIST: 'List',
    SINGLE: 'Single Item'
};

const scheduleType = {
    SCHEDULED: 'Scheduled',
    DAILY: 'Daily',
    WEEKLY: 'Weekly',
    MONTHLY: 'Monthly'
};

/**
 * Money Jar Fields
 */
let selectedMoneyJarType;
let emptyExpenseList;

/**
 * Pie Chart components
 */
let jarsCanvas;
let jarsLegends;

let expenseTypeElem;
let amountElem;
let time;
let date;

let moneyJarIdModal;

let fldPaybill;

function configureMoneyJar() {
//    fldPaybill = document.getElementById("fldPaybill");
    
    errorMessage = document.getElementById("jarModalError");
    errorMessageRemover = document.getElementById("removeErrorMessage");
    errorMessageRemover.addEventListener("click", function(event) {
        errorMessage.hidden = true;
    });
    
    let expenseType = document.getElementById("expenseType");
    let expenseTypeKeys = Object.keys(expenseTypes);
    expenseTypeKeys.forEach(expense => {
        let option = document.createElement("option");
        option.value = expense;
        option.innerHTML = expense;
        expenseType.appendChild(option);
    });
    
    // Initialize HTML elements
    emptyExpenseList = document.getElementById("emptyExpenseList");
    moneyJarIdModal = document.getElementById("moneyJarIdModal");
    
    expenseTypeElem = document.getElementById("expenseType");
    expenseTypeElem.addEventListener("change", function(event) {
        let spanType = document.querySelector("span[for='expenseType']");
        spanType.textContent = "";
        spanType.style.display = "none";
        
        let cat = expenseTypeElem.options[expenseTypeElem.selectedIndex].value;
        if (cat === "Groceries") {
            categorySelector.options[0].selected = true;
            grocerySection.hidden = false;
            expenseSection.hidden = true;
        } else if (cat === "Personal") {
            categorySelector.options[0].selected = true;
            grocerySection.hidden = false;
            expenseSection.hidden = true;
        } else {
            categorySelector.options[1].selected = true;
            grocerySection.hidden = true;
            expenseSection.hidden = false;
        }
    });
    
    amountElem = document.getElementById("totalAmount");
    time = document.getElementById("scheduledHour");
    time.addEventListener("input", function (event) {
        let spanHour = document.querySelector("span[for='scheduledHour']");
        spanHour.textContent = "";
        spanHour.style.display = "none";
    });
    date = document.getElementById("scheduledDate");
    date.addEventListener("input", function() {
        let spanDate = document.querySelector("span[for='scheduledDate']");
        spanDate.textContent = "";
        spanDate.style.display = "none";
    });
    
    btnOpenJarModal = document.getElementById("btnOpenJarModal");
    btnOpenJarModal.onclick = function() {
        openJarModal(null);
    };

    // closing modal
    cancelJarModal = document.getElementById("cancelJarModal");
    cancelJarModal.onclick = function() {
        jarModal.style.display = "none";
    };

    btnSubmitJar = document.getElementById("btnSaveJar");
//    btnSubmitJar.onclick = function() {
//        sendJarRequest();
//    };

    // Grocery section
    grocerySection = document.getElementById("groceries");
    // Jar sections
    expenseSection = document.getElementById("expense");
    expenseItemsSection = document.getElementById("expenseContainer");

    categorySelector = document.getElementById("categorySelector");
    categorySelector.options[1].selected = true;
    
    categorySelector.addEventListener("change", function () {
        selectedMoneyJarType = categorySelector.options[categorySelector.selectedIndex].value;
        if (selectedMoneyJarType === categoryOption.LIST) {
            grocerySection.hidden = false;
            expenseSection.hidden = true;
            
            let type = expenseTypeElem.options[expenseTypeElem.selectedIndex].value;
            if (type !== "Groceries") {
                errorMessage = document.getElementById("jarModalError");
                error.innerHTML = "Only Groceries and Personal expense types are allowed to have lists";
                categorySelector.options[1].selected = true;
                
                grocerySection.hidden = true;
                expenseSection.hidden = false;
            } else if (type !== "Personal") {
                var error = document.getElementById("jarModalError");
                error.innerHTML = "Only Groceries and Personal expense types are allowed to have lists";
                categorySelector.options[1].selected = true;
                
                grocerySection.hidden = true;
                expenseSection.hidden = false;
            }
        } else if (selectedMoneyJarType === categoryOption.SINGLE) {
            grocerySection.hidden = true;
            expenseSection.hidden = false;
            
            let type = expenseTypeElem.options[expenseTypeElem.selectedIndex].value;
            if (type === "Groceries") {
                var error = document.getElementById("jarModalError");
                error.innerHTML = "Groceries should not have single item expense type";
                categorySelector.options[0].selected = true;
                
                grocerySection.hidden = false;
                expenseSection.hidden = true;
            }
        }
    });

    // Define event for time picker

    dayOfWeek = document.getElementById("dayOfWeek");
    timePicker = document.getElementById("timePicker");
    datePicker = document.getElementById("datePicker");

    scheduleSelector = document.getElementById("scheduleSelector");
    scheduleSelector.addEventListener("change", function () {
        let selected = scheduleSelector.options[scheduleSelector.selectedIndex].value;
        if (selected === scheduleType.WEEKLY) {
            // weekly notification
            dayOfWeek.hidden = false;
            timePicker.hidden = false;
            datePicker.hidden = true;
        } else if (selected === scheduleType.SCHEDULED || selected === scheduleType.MONTHLY) {
            // scheduled notification - happens only once when scheduled
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

    jarTemplate = document.getElementById("jarTemplate");
    jarList = document.getElementById("jarList");

    getAllMoneyJars();

    jarsCanvas = document.getElementById("jarsCanvas");
    jarsCanvas.width = 300;
    jarsCanvas.height = 300;
    jarsLegends = document.querySelector("legend[for='jarsCanvas']");

    // register moneyJar change listener
    jars.registerListener(function (val) {
        
    });
}

function getAllMoneyJars() {
    if (document.getElementById("progress").hidden) {
        document.getElementById("progress").hidden = false;
    }
    let request = getXmlHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            
            document.getElementById("progress").hidden = true;
            if (request.status === 200) {
                jarCount = 0;
                notificationCount = 0;

                let json = request.responseText;
                let jarsDto = JSON.parse(json);
                let jarDtoList = jarsDto.jar_elements;

                if (jarDtoList.length > 0) {
                    emptyExpenseList.hidden = true;
                }
                
                notificationContainer.innerHTML = "";
                notificationContainer.appendChild(notificationTemplate);
                
                jarList.innerHTML = "";
                jarList.appendChild(jarTemplate);
                
                jars.removeAll();

                // update the global list; list is empty
                for (let i = 0; i < jarDtoList.length; i++) {
                    let moneyJarDto = jarDtoList[i];
                    let jar = moneyJarDto.jar;

                    addJarToList(moneyJarDto);
                }
                
//                reload canvas
//                jarsCanvas = document.getElementById("jarsCanvas");
//                document.removeChild(jarsCanvas);
//                
//                jarsCanvas = document.createElement("canvas");
//                jarsCanvas.id = "jarsCanvas";
//                
//                jarsCanvas.width = 300;
//                jarsCanvas.height = 300;
                
                transactionTBody.innerHTML = "";
                getAllTransactions();
            } else if (request.status === 404) {
                notificationContainer.innerHTML = "";
                notificationContainer.appendChild(notificationTemplate);
                
                jarList.innerHTML = "";
                jarList.appendChild(jarTemplate);
                
                emptyExpenseList.hidden = false;
                
                transactionTBody.innerHTML = "";
                getAllTransactions();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else {
                console.log("Server could not find what you are looking for.")
            }
        }
    };

    let data = userFields.TOKEN + "=" + escape(window.localStorage.getItem("token")) + "&";
    data += userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));

    request.open("GET", ctx + "/dashboard/jars-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

function activateTimer(jarId) {
    let jarDto = jars.getJar(jarId);
    let jar = jarDto.jar;
    let type = jar.expense_type;
    
    let scheduled = new Date(jar.scheduled_for);
    let now = new Date();
    let timeReached = scheduled.getTime() <= now.getTime();
    if (jar.jar_status) {
        populateNotificationSection(jarId);
        return;
    }
    
    (function(key, dayScheduled, jarStatus) {
        let today = new Date();
        let timeScheduled = new Date(dayScheduled);

        let isTimeReached = timeScheduled.getTime() <= today.getTime();
        if (isTimeReached && !jarStatus) {
            showNotificationDialog(key, jarDto);
            return;
        }else if (isTimeReached && jarStatus) {
            populateNotificationSection(key);
            return;
        }

        window.setTimeout(arguments.callee, 1000, key, dayScheduled, jarStatus);
    })(jarId, jar.scheduled_for, jar.jar_status);
}

/**
 * Updates the expense based on the last time they had logged in
 * 
 * @param {type} jarId identifies the expense in question
 * @param {int} time time difference since the last the last schedule of the expense.
 * @param {Date} newDate the date to which the expense should be updated to
 */
function updatePayments(jarId, time, newDate) {
    let moneyJarDto = jars.getJar(jarId);
    let jar = moneyJarDto.jar;
    
    let totalAmount = jar.amount * time;
    for (var i = 0; i < time; i++) {
        updateMoneyJarJson(jarId);
    }
    
    sendJarRequestJson(moneyJarDto, newDate);
}

function drawPieChart(jarElements) {
    let jarMap = {};
    let expenseTypeMap = {};
    let count = 0;
    jarElements.forEach(jarElement => {
        if (!isInExpenseTypeMap(jarElement.jar.expense_type, expenseTypeMap) && !jarElement.jar.jar_status) {
            expenseTypeMap[jarElement.jar.expense_type] = count;
            jarMap[count] = jarElement.jar;
            count++;
        }
    });

    let jarItems = Object.values(jarMap);
    let colors = ["#F3004D", "#4E4D4A", "#333333", "#74B100", "#353432", "#85C760", "#008FB4", "#F8FFE1", "#294875", "#AA002E"]
    let properties = {
        canvas: jarsCanvas,
        jars: jarItems,
        colors: colors
    };

    let pieChart = new PieChart(properties);
    pieChart.draw();
}

function isInExpenseTypeMap(expenseType, map) {
    let keys = Object.keys(map);
    
    // return false if map is empty
    if (keys.length === 0) {
        return false;
    }
    
    keys.forEach(key => {
        if (key === expenseType) {
            return true;
        }
    });
    return false;
}

/**
 * Validates input from the user
 */
function validateInput(isExpenseEdit) {    
    let validHour = true;
    if (time.value === '') {
        let spanHour = document.querySelector("span[for='scheduledHour']");
        spanHour.style.color = "#AA002E";
        spanHour.style.fontSize = "12px";
        spanHour.textContent = "Time input field is empty";
        spanHour.style.display = "block";
        validHour = false;
    } else if (time.value !== '' && !isExpenseEdit) {
        let timeNow = new Date().subtractMinutes(new Date(), 1);
        let timeSet = new Date(date.value + " " + time.value);
        if (timeSet < timeNow) {
            let spanHour = document.querySelector("span[for='scheduledHour']");
            spanHour.style.color = "#AA002E";
            spanHour.style.fontSize = "12px";
            spanHour.textContent = "Invalid time(allowed time: one minute behind or a time in future)";
            spanHour.style.display = "block";
            validHour = false;
        } else {
            validHour = true;
        }
    }
    
    
    let validDate = true;
    let dPicker = document.getElementById("datePicker");
    if (date.value === '' && !dPicker.hidden) {
        let spanDate = document.querySelector("span[for='scheduledDate']");
        spanDate.style.color = "#AA002E";
        spanDate.style.fontSize = "12px";
        spanDate.textContent = "Date input field is empty";
        spanDate.style.display = "block";
        validHour = false;
    } else if (date.value !== '' && !isExpenseEdit) {
        let dateNow  = new Date();
        if (!dPicker.hidden && new Date(date.value).getDate() < dateNow.getDate()) {
            let spanDate = document.querySelector("span[for='scheduledHour']");
            spanDate.style.color = "#AA002E";
            spanDate.style.fontSize = "12px";
            spanDate.textContent = "Invalid date input (Requires date set to be today or in future)";
            spanDate.style.display = "block";
            validDate = false;
        } else if (dPicker.hidden) {
            date.value = "";
            validDate = true;
        } else {
            validDate = true;
        }
    }
    
    let expenseTypeSelected = true;
    if (expenseTypeElem.selectedIndex === 0) {
        let spanType = document.querySelector("span[for='expenseType']");
        spanType.style.color = "#AA002E";
        spanType.style.fontSize = "12px";
        spanType.textContent = "You have not selected expense type";
        spanType.style.display = "block";
        expenseTypeSelected = false;
    } else {
        expenseTypeSelected = true;
    }
    
    if (categorySelector.options[0].selected) {
        // list of expenses section
        let validList = true;
        expenseAmount.value = "";
        let groceryKeys = Object.keys(groceryListObj);
        if (groceryKeys.length < 1) {
            let listSpan = document.querySelector("span[for='groceryContainer']");
            listSpan.style.color = "#AA002E";
            listSpan.style.fontSize = "12px";
            listSpan.textContent = "This list should not be empty";
            listSpan.style.display = "block";
            validList = false;
        } else {
            validList = true;
        }
        
//        let validPayBill = true;
//        if (fldPaybill.value === '') {
//            validPayBill = true;
//        } else {
//            let payBill = fldPaybill.value;
//            if (payBill.length <= 4 || payBill.length >= 7) {
//                validPayBill = false;
//            } else {
//                validPayBill = true;
//            }
//        }
        
        return validHour && validDate && expenseTypeSelected && validList;
    } else {
        // Single expense section
        let validAmount = true;
        if (expenseAmount.value === '' || expenseAmount.value < 5) {
            let spanAmount = document.querySelector("span[for='expenseAmount']");
            spanAmount.style.color = "#AA002E";
            spanAmount.style.fontSize = "12px";
            spanAmount.textContent = "Expense amount field must not be empty or less than 5";
            spanAmount.style.display = "block";
            validAmount = false;
        } else {
            validAmount = true;
        }

        let payeeNameSelected = true;
        if (expenseTypeElem.selectedIndex === 0) {
            payeeName.options[1].selected = true;
            payeeNameSelected = true;
        }
        
        return validAmount && validHour && validDate && expenseTypeSelected && payeeNameSelected;
    }
}

function openJarModal(callback) {
    moneyJarIdModal.textContent = "";
//    clear the grocery list
    groceryListObj = {};
    
    groceryListTBody.innerHTML = "";
    
    let spanAmount = document.querySelector("span[for='expenseAmount']");
    spanAmount.textContent = "";
    spanAmount.style.display = "none";
    
    let modalTitle = document.getElementById("modalTitle");
    modalTitle.innerHTML = "Add Expenses (Reminder)";
    
    jarModal.style.display = "block";
    
    btnEditExpense = document.getElementById("btnEditExpense");
    btnEditExpense.hidden = true;
    btnDeleteExpense = document.getElementById("btnDeleteExpense");
    btnDeleteExpense.hidden = true;
    
    var btnOpenGroceryModal = document.getElementById("btnOpenGroceryModal");
    btnOpenGroceryModal.hidden = false;
    
    expenseTypeElem.options[0].selected = true;
    expenseTypeElem.disabled = false;
    
    amountElem.innerHTML = "0";
    time.value = "";
    time.disabled = false;
    
    date.value = "";
    date.disabled = false;
    
    categorySelector.options[1].selected = true;
    categorySelector.disabled = false;
    
    scheduleSelector.options[0].selected = true;
    scheduleSelector.disabled = false;
    
    daySelector.options[0].selected = true;
    daySelector.disabled = false;
    
    businessNumber.value = "";
    businessNumber.disabled = false;
    
    payeeAccountNumber.value = "";
    payeeAccountNumber.disabled = false;
    
    payeeName.options[0].selected = true;
    payeeName.disabled = false;
    
    expenseAmount.value = "";
    expenseAmount.disabled = false;
    
    cancelJarModal.addEventListener("click", function (event) {
        jarModal.style.display = "none";
    });

    btnSubmitJar.value = "Submit";
    btnSubmitJar.onclick = function() {
        if (!validateInput()) {
            return;
        }
        addExpense(callback);
    };
}

/**
 * Open expense dialog box
 * @param {type} jarId allows users delete or update the expense
 */
let btnDeleteExpense;
function openJarModalForEdit(jarId) {
    //    clear the grocery list
    groceryListObj = {};
    
    groceryListTBody.innerHTML = "";
    
    let spanAmount = document.querySelector("span[for='expenseAmount']");
    spanAmount.textContent = "";
    spanAmount.style.display = "none";
    
    let modalTitle = document.getElementById("modalTitle");
    modalTitle.innerHTML = "Edit Expense";
    
    btnEditExpense = document.getElementById("btnEditExpense");
    btnEditExpense.hidden = true;
    
    btnDeleteExpense = document.getElementById("btnDeleteExpense");
    btnDeleteExpense.hidden = false;
    btnDeleteExpense.addEventListener("click", function(event) {
        deleteExpense(jarId);
    });
    
    jarModal.style.display = "block";

    let jarDto = jars.getJar(jarId);
    let jar = jarDto.jar;
    
    moneyJarIdModal.value = jarId;
    
    // Set inputs to be changed
    let expenseOptions = expenseTypeElem.options;
    
    for (var i = 0; i < expenseOptions.length; i++) {
        let option = expenseOptions[i];
        if (jar.expense_type === option.value) {
            expenseTypeElem.options[i].selected = true;
            expenseTypeElem.disabled= false;
            break;
        }
    }
    
    amountElem.innerHTML = jar.amount;
    let dateArr = jar.scheduled_for.split(" ");
    let dateSet = new Date();
    date.value = dateArr[0];
    date.disabled = false;
    
    time.value = dateArr[1];
    time.disabled = false;
    
    var btnOpenGroceryModal = document.getElementById("btnOpenGroceryModal");
    btnOpenGroceryModal.hidden = false;

    amountElem.innerHTML = jar.amount;

    if (jar.category === categoryOption.LIST) {
        categorySelector.options[0].selected = true;
        let groceries = jarDto.groceries;

        setGroceries(groceries);

        expenseSection.hidden = true;
        grocerySection.hidden = false;
    } else {
        categorySelector.options[1].selected = true;

        setExpenseFields(jarId);
        expenseAmount.disabled = false;
        payeeName.disabled = false;
        businessNumber.disabled = false;
        payeeAccountNumber.disabled = false;

        expenseSection.hidden = false;
        grocerySection.hidden = true;
    }
    categorySelector.disabled = false;

    if (jar.scheduled_type === "scheduled") {
        scheduleSelector.options[0].selected = true;
    } else if (jar.scheduled_type === "Daily") {
        scheduleSelector.options[1].selected = true;
    } else if (jar.scheduled_type === "Weekly") {
        scheduleSelector.options[2].selected = true;
        setDay(jar.scheduled_for);
    } else if (jar.scheduled_type === "Monthly") {
        scheduleSelector.options[3].selected = true;
    }
    scheduleSelector.disabled = false;

    btnSubmitJar.value = "Submit";
    btnSubmitJar.classList.add("btn2");
    btnSubmitJar.onclick = function() {
        if (!validateInput(true)) {
            return;
        }
        updateMoneyJar(jarId);
    };
    
    var btnOpenGroceryModal = document.getElementById("btnOpenGroceryModal");
    btnOpenGroceryModal.hidden = false;
    
    cancelJarModal.addEventListener("click", function (event) {
        btnDeleteExpense.hidden = true;
        jarModal.style.display = "none";
    });
}

/**
 * Open expense dialog box
 * @param {type} jarId allows users delete or update the expense
 */
let btnEditExpense;
function openJarModalForPay(jarId, isPaid) {
    //    clear the grocery list
    groceryListObj = {};
    groceryListTBody.innerHTML = "";
    
    let spanAmount = document.querySelector("span[for='expenseAmount']");
    spanAmount.textContent = "";
    spanAmount.style.display = "none";
    
    let modalTitle = document.getElementById("modalTitle");
    modalTitle.innerHTML = "Pay for Expense";
    
    btnEditExpense = document.getElementById("btnEditExpense");
    btnEditExpense.hidden = false;
    btnEditExpense.addEventListener("click", function (event) {
        jarModal.style.display = "none";
        openJarModalForEdit(jarId);
    });
    
    btnDeleteExpense = document.getElementById("btnDeleteExpense");
    btnDeleteExpense.hidden = false;
    btnDeleteExpense.addEventListener("click", function(event) {
        deleteExpense(jarId);
    });
    
    jarModal.style.display = "block";

    let jarDto = jars.getJar(jarId);
    let jar = jarDto.jar;
    
    moneyJarIdModal.value = jarId;
    
    // Set inputs to be changed
    let expenseOptions = expenseTypeElem.options;
    
    for (var i = 0; i < expenseOptions.length; i++) {
        let option = expenseOptions[i];
        if (jar.expense_type === option.value) {
            expenseTypeElem.options[i].selected = true;
            expenseTypeElem.disabled = true;
            break;
        }
    }
    
    amountElem.innerHTML = jar.amount;
    let dateArr = jar.scheduled_for.split(" ");
    date.value = dateArr[0];
    date.disabled = true;
    
    time.value = dateArr[1];
    time.disabled = true;

    amountElem.innerHTML = jar.amount;

    if (jar.category === categoryOption.LIST) {
        categorySelector.options[0].selected = true;
        let groceries = jarDto.groceries;

        setGroceries(groceries);

        expenseSection.hidden = true;
        grocerySection.hidden = false;
    } else {
        categorySelector.options[1].selected = true;

        setExpenseFields(jarId);
        expenseAmount.disabled = true;
        payeeName.disabled = true;
        businessNumber.disabled = true;
        payeeAccountNumber.disabled = true;

        expenseSection.hidden = false;
        grocerySection.hidden = true;
    }
    categorySelector.disabled = true;

    if (jar.scheduled_type === "scheduled") {
        scheduleSelector.options[0].selected = true;
    } else if (jar.scheduled_type === "Daily") {
        scheduleSelector.options[1].selected = true;
    } else if (jar.scheduled_type === "Weekly") {
        scheduleSelector.options[2].selected = true;
        setDay(jar.scheduled_for);
    } else if (jar.scheduled_type === "Monthly") {
        scheduleSelector.options[3].selected = true;
    }
    scheduleSelector.disabled = true;
    
    var btnOpenGroceryModal = document.getElementById("btnOpenGroceryModal");
    btnOpenGroceryModal.hidden = true;

    btnSubmitJar.value = "Pay";
    btnSubmitJar.onclick = function() {
        makePayments(moneyJarIdModal.value);
    };
    
    if (isPaid) {
        btnSubmitJar.onclick = function() {};
        btnSubmitJar.style.backgroundColor = "#00b3ff";
    }
    
    cancelJarModal.addEventListener("click", function (event) {
        btnDeleteExpense.hidden = true;
        jarModal.style.display = "none";
    });
}

/**
 * Delete expense from backend.
 * @param {type} jarId identifies the expense
 */
function deleteExpense(jarId) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                jars.deleteJar(jarId);

                jarModal.style.display = "none";
                
                getAllMoneyJars();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else {
                showError();
            }
        }
    };

    jarId = jars.getJar(jarId).jar.jar_id;
    let token = window.localStorage.getItem(userFields.TOKEN);
    let data = jarFields.JAR_ID + "=" + jarId;

    request.open("DELETE", ctx + "/api/jars/delete-money-jar?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.APPLICATION_JSON);
    request.setRequestHeader(requestHeader.AUTHORIZATION, "Bearer " + token);
    request.send();
}

/**
 * select the day of the week for that schedule.
 * 
 * @param {string} day weekday: Monday, Tuesday, Wednesday, etc
 */
function setDay(day) {
    if (day === "Monday") {
        daySelector.options[0].selected = true;
    } else if (day === "Tuesday") {
        daySelector.options[1].selected = true;
    } else if (day === "Wednesday") {
        daySelector.options[2].selected = true;
    } else if (day === "Thursday") {
        daySelector.options[3].selected = true;
    } else if (day === "Friday") {
        daySelector.options[4].selected = true;
    } else if (day === "Saturday") {
        daySelector.options[5].selected = true;
    } else if (day === "Sunday") {
        daySelector.options[0].selected = true;
    } else {
        daySelector.options[0].selected = true;
    }
}

/**
 * sends a request throught the controller to add an item to grocery
 */
//function sendJarRequest() {
//    var request = getXmlHttpRequest();
//
//    request.onreadystatechange = function() {
//        if (request.readyState === 4) {
//            if (request.status === 200) {
//                var json = request.responseText;
//                let moneyJarDto = JSON.parse(json);
//                addJarToList(moneyJarDto);
//
//                jarModal.style.display = "none";
//            } else {
//                showError();
//            }
//        }
//    };
//
//    var data = serializeData();
//
//    request.open("PUT", ctx + "/dashboard/jars-controller?" + data, true);
//    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
//    request.send();
//}

/**
 * sends request to API to add an item - called when updating jar schedule
 * @param {JarDto} jarDto object containing items to be added
 * @param {string} date date to modify the query
 */
function sendJarRequestJson(jarDto, date) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                var json = request.responseText;
                let moneyJarDto = JSON.parse(json);
//                addJarToList(moneyJarDto);

                jarModal.style.display = "none";

                getAllMoneyJars();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else {
                showError();
            }
        }
    };
    
    let jar = {
        amount: jarDto.jar.amount,
        category: jarDto.jar.category,
        expense_type: jarDto.jar.expense_type,
        created_at: jarDto.jar.created_at,
        household_id: jarDto.jar.household_id,
        jar_id: jarDto.jar.jar_id,
        jar_status: jarDto.jar.jar_status,
        payment_status: jarDto.jar.payment_status,
        scheduled_for: date,
        scheduled_type: jarDto.jar.scheduled_type
    };

    let dataSending = {
        jar: jar
    };
    let data = JSON.stringify(dataSending);
    let token = window.localStorage.getItem(userFields.TOKEN);

    request.open("PUT", ctx + "/api/jars/add-jar", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.APPLICATION_JSON);
    request.setRequestHeader(requestHeader.AUTHORIZATION, "Bearer " + token);
    request.send(data);
}

/**
 * send new jar elements
 * @param {modal} callback to call required modals subsequently
 */
function addExpense(callback) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                var json = request.responseText;
                let moneyJarDto = JSON.parse(json);

//                addJarToList(moneyJarDto);
                getAllMoneyJars();
                
                if (callback !== null) {
                    delete incomplete[callback.key];

                    let len = Object.keys(incomplete).length;

                    if (len > 1) {
                        callback.onNext(callback);
                    } else if(len === 1) {
                        callback.onDone(callback);
                    } else {
                        callback.onComplete();
                    }
                }
                
                jarModal.style.display = "none";
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else {
                showError();
            }
        }
    };

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/jars-controller/add-money-jar?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

/**
 * Update remote element when user choose to update/edit jar elements
 * 
 * @param {String} jarId key value of an element in moneyJarList
 */
function updateMoneyJar(jarId) {

    // Update remote record
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                let json = request.responseText;
                let moneyJarDto = JSON.parse(json);
//                addJarToList(moneyJarDto);

                jarModal.style.display = "none";
                
                getAllMoneyJars();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else {
                showError();
            }
        }
    };

    let data = serializeData();

    request.open("PUT", ctx + "/dashboard/jars-controller/update-money-jar?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

/**
 * updates the backend by JSON
 * @param {type} jarId identifies the jar
 */
function updateMoneyJarJson(jarId) {

    // Update remote record
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                
                let json = request.responseText;
                let moneyJarDto = JSON.parse(json);
                
//                jars.setJar(moneyJarDto.schedule_id, moneyJarDto);
//                addJarToList(moneyJarDto);
//                getAllMoneyJars();
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else {
                showError();
            }
        }
    };

    let jarDto = jars.getJar(jarId);
    
    let data = JSON.stringify(jarDto);
    let token = window.localStorage.getItem(userFields.TOKEN);

    request.open("PUT", ctx + "/api/jars/update-jar", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.APPLICATION_JSON);
    request.setRequestHeader(requestHeader.AUTHORIZATION, "Bearer " + token);
    request.send(data);
}

/**
 * Add the item added by the user to aa global list variable and populates the jar schedule section
 * @param {string} moneyJarDto contains a list of jars with elements
 */
function addJarToList(moneyJarDto) {
    let jar = moneyJarDto.jar;
    // get length of the list
    let beforeLen = Object.keys(jars.getJarList()).length;

//    add to global list
    let jarId = moneyJarDto.schedule_id;
    jars.setJar(jarId, moneyJarDto);
    
    // start a timer for the jar schedule
    activateTimer(jarId);

    // check length after adding item
    let afterLen = Object.keys(jars.getJarList()).length;

    if (afterLen > beforeLen) {
        setJars(jarId);
    }
    
    emptyExpenseList.hidden = true;
    
    let values = Object.values(jars.getJarList());
    drawPieChart(values);
}

/**
 * Update the ui with data from server about jars
 * @param {String} jarId id that identifies that added jar
 */
function setJars(jarId) {

    // let rows = moneyJarTbody.rows;
    let keys = Object.keys(jars.getJarList());
    if (jarId === "") {

        keys.forEach(key =>  {
            let jarDto = jars.getJar(key);
            let jar = jarDto.jar;
            if (!jar.jar_status) {
                updateJarTable(jarDto);
            }
        });
    } else {
        
        let jarDto = jars.getJar(jarId);
        let jar = jarDto.jar;
        if (!jar.jar_status) {
            addToJarTable(jarDto);
        }
    }
    // update the table with new item
}

/**
 * reload the jar table with the name of the jar and scheduled date
 * @param {MoneyJarDto} jarDto contains all items of the jar
 * @param {Integer} index represents the row to update
 */
let jarCount = 0;
function updateJarTable(jarDto) {
    let jar = jarDto.jar;
    let clone = jarTemplate.content.cloneNode(true);

    clone.querySelector("#jarId").id += jarCount;
    let idTemplate = clone.querySelector("#jarId" + jarCount);
    idTemplate.value = jarDto.schedule_id;
    
    clone.querySelector("#expType").id += jarCount;
    let type = clone.querySelector("#expType" + jarCount);
    type.textContent = jar.expense_type;

    clone.querySelector("#scheduleInterval").id += jarCount;
    let interval = clone.querySelector("#scheduleInterval" + jarCount);
    interval.textContent = jar.scheduled_type;

    clone.querySelector("#scheduleDate").id += jarCount;
    let jarDate = clone.querySelector("#scheduleDate" + jarCount);
    jarDate.textContent = jar.scheduled_for;

    clone.querySelector("#jarAmount").id += jarCount;
    let amount = clone.querySelector("#jarAmount" + jarCount);
    amount.textContent = jar.amount;

    clone.querySelector("#jarItem").id += jarCount;
    let jarBudgetItem = clone.querySelector("#jarItem" + jarCount);
    jarBudgetItem.addEventListener("click", function(event) {
        let idIndex = event.target.id[event.target.id.length - 1];
        let jarId = document.getElementById("jarId" + idIndex);
        openJarModalForEdit(jarId.value);
    });

    jarList.appendChild(clone);

    jarCount++;
}

/**
 * update the jar table with the name of the jar and scheduled date
 * @param {Jar} jar contains all items of the jar
 * @param {Integer} index represents the row to update
 */
function addToJarTable(jarDto) {
    let jar = jarDto.jar;
    
    let clone = jarTemplate.content.cloneNode(true);

    clone.querySelector("#jarId").id += jarCount;
    let idTemplate = clone.querySelector("#jarId" + jarCount);
    idTemplate.value = jarDto.schedule_id;
    
    clone.querySelector("#expType").id += jarCount;
    let type = clone.querySelector("#expType" + jarCount);
    type.textContent = jar.expense_type;

    clone.querySelector("#scheduleInterval").id += jarCount;
    let interval = clone.querySelector("#scheduleInterval" + jarCount);
    interval.textContent = jar.scheduled_type;

    clone.querySelector("#scheduleDate").id += jarCount;
    let jarDate = clone.querySelector("#scheduleDate" + jarCount);
    jarDate.textContent = jar.scheduled_for;

    clone.querySelector("#jarAmount").id += jarCount;
    let amount = clone.querySelector("#jarAmount" + jarCount);
    amount.textContent = jar.amount;

    clone.querySelector("#jarItem").id += jarCount;
    let jarBudgetItem = clone.querySelector("#jarItem" + jarCount);
    jarBudgetItem.addEventListener("click", function(event) {
        let idIndex = event.target.id[event.target.id.length - 1];
        let jarId = document.getElementById("jarId" + idIndex);
        openJarModalForEdit(jarId.value);
    });

    jarList.appendChild(clone);

    jarCount++;
}

/**
 * Get input data from the user and serialize
 */
function serializeData() {
    // set user id details
    var userId = escape(window.localStorage.getItem("user_id"));
    var token = escape(window.localStorage.getItem("token"));
    let liabilitiesObj = {};

    var jarModalError = document.getElementById("jarModalError");
//    if (expenseTypeElem.value === "") {
//        jarModalError.innerHTML = "This field is required: Jar Name";
//        return;
//    }
    
    // get list depending on category
    if (selectedMoneyJarType === undefined) {
        selectedMoneyJarType = categorySelector.options[categorySelector.selectedIndex].value;
    }
    
    let items;
    if (selectedMoneyJarType === categoryOption.SINGLE) {
        liabilitiesObj.expense = expenseGlobal;
        items = JSON.stringify(liabilitiesObj);
    } else if (selectedMoneyJarType === categoryOption.LIST) {
        expenseListDto.groceries = Object.values(groceryListObj);
        items = JSON.stringify(expenseListDto);
    }

    if (items === "" && selectedMoneyJarType === categoryOption.LIST) {
        jarModalError.innerHTML = "Your item list is empty";
        return;
    }
    
    let jarId = moneyJarIdModal.value;

    let data = userFields.TOKEN + "=" + token + "&";
    data += userFields.USER_ID + "=" + userId + "&";
    data += jarFields.JAR_ID + "=" + escape(jarId) + "&";
    data += jarFields.EXPENSE_TYPE + "=" + escape(expenseTypeElem.options[expenseTypeElem.selectedIndex].value) + "&";
    data += jarFields.CATEGORY + "=" + escape(selectedMoneyJarType) + "&";
    data += jarFields.TOTAL_AMOUNT + "=" + escape(amountElem.innerHTML) + "&";
    
    // get date
    let dateTime;
    let scheduledType = "";
    let selected = scheduleSelector.options[scheduleSelector.selectedIndex].value;
    if (selected === scheduleType.WEEKLY) {
        if (daySelector.selectedIndex === new Date().getDay()) {
            dateTime = formatDate(new Date());
        } else if (daySelector.selectedIndex > new Date().getDay()) {
            let dayDiff = daySelector.selectedIndex - new Date().getDay();
            let date = new Date().addDays(new Date().toDateString(), dayDiff);
            dateTime = formatDate(date);
        } else if (daySelector.selectedIndex < new Date().getDay()) {
            let dayDiff = 7 - new Date().getDay() + daySelector.selectedIndex;
            let date = new Date().addDays(new Date().toDateString(), dayDiff);
            dateTime = formatDate(date);
        }
        dateTime += " " + time.value;
        scheduledType = scheduleType.WEEKLY;
    } else if (selected === scheduleType.SCHEDULED) {
        dateTime = date.value === "" ? "" : date.value;
        dateTime += " " + (time.value === "" ? "" : time.value);
        scheduledType = scheduleType.SCHEDULED;
    } else if (selected === scheduleType.MONTHLY) {
        let newDate = new Date(date.value);
        let timeStr = formatDate(newDate) + " " + time.value;
        let newTime = new Date(timeStr);
        
        dateTime = formatDate(newDate);
        dateTime += " " + formatTime(newTime);
        
        scheduledType = scheduleType.MONTHLY;
    } else {
        dateTime = formatDate(new Date()) + " " + time.value;
        scheduledType = scheduleType.DAILY;
    }

    data += jarFields.SCHEDULE + "=" + escape(dateTime) + "&";
    data += jarFields.SCHEDULED_TYPE + "=" + escape(scheduledType) + "&";
    data += jarFields.LIABILITIES + "=" + escape(items);

    return data;
}

/**
 * Update moneyJar Dto's total amount - used when adding an item or expense.
 * @param {double} amount total amount of comodity
 */
function onRowLoaded(amount) {
    var currentTotal = amountElem.innerHTML;
    currentTotal = parseFloat(amount) + parseFloat(currentTotal);

    amountElem.innerHTML = currentTotal;
}

/**
 * subtract from total and update modal's amount
 * @param {double} amount total amount of comodity
 */
function onRowRemoved(amount) {
    var currentTotal = amountElem.innerHTML;
    currentTotal = parseFloat(currentTotal) - parseFloat(amount);

    amountElem.innerHTML = currentTotal;
}

/**
 * Update moneyJar Dto's total amount - used when adding an expense.
 * @param {double} amount total amount of comodity
 */
function onRowLoadedExpenses(amount) {
    amountElem.innerHTML = amount;
}

/**
 * Subtracts the previous amount from the total amount then add the new amount to the subtracted total.
 * 
 * @param {string} previous amount of comodity before item change
 * @param {string} newPrice amount of comodity after item changed
 */
function updateTotalAmount(previous, newPrice) {
    var currentTotal = amountElem.innerHTML;
    currentTotal = parseInt(currentTotal) - parseInt(previous);
    currentTotal += parseInt(newPrice);

    amountElem.innerHTML = currentTotal;
}