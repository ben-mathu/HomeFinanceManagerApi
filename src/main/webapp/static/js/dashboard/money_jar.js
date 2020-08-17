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
let groceryListSection;
let expenseItemsSection;

let dayOfWeek;

let jars = {
    moneyJarList: {},
    moneyJarListListener: function(val) {
        
    },
    setJar: function(id, val) {
        this.moneyJarList[id] = val;
        this.moneyJarListListener(val);
    },
    getJar: function(val) {
        return this.moneyJarList[val];
    },
    getJarList: function() {
        return this.moneyJarList;
    },
    registerListener: function(listener) {
        this.moneyJarListListener = listener;
    }
}

// templating
let jarTemplate;
let jarList;

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
 * Money Jar Fields
 */
let selectedMoneyJarType;

/**
 * Pie Chart components
 */
let jarsCanvas;
let jarsLegends;

let jarLabel;
let amountElem;
let time;
let date;

let moneyJarIdModal;

function configureMoneyJar() {
    // Initialize HTML elements
    moneyJarIdModal = document.getElementById("moneyJarIdModal");
    jarLabel = document.getElementById("jarLabel");
    amountElem = document.getElementById("totalAmount");
    time = document.getElementById("scheduledHour");
    date = document.getElementById("scheduledDate");
    
    btnOpenJarModal = document.getElementById("btnOpenJarModal");
    btnOpenJarModal.onclick = function() {
        jarModal.style.display = "block";
    }

    // closing modal
    cancelJarModal = document.getElementById("cancelJarModal");
    cancelJarModal.onclick = function() {
        jarModal.style.display = "none";
    }

    btnSubmitJar = document.getElementById("btnSaveJar");
    btnSubmitJar.onclick = function() {
        sendJarRequest();
    }

    // Grocery section
    grocerySection = document.getElementById("groceries");
    groceryListSection = document.getElementById("groceryContainer");
    // Jar sections
    expenseSection = document.getElementById("expense");
    expenseItemsSection = document.getElementById("expenseContainer");

    categorySelector = document.getElementById("categorySelector");
    categorySelector.addEventListener("change", function () {
        selectedMoneyJarType = categorySelector.options[categorySelector.selectedIndex].value;
        if (selectedMoneyJarType == categoryOption.GROCERIES) {
            grocerySection.hidden = false;
            expenseSection.hidden = true;
        } else if (selectedMoneyJarType == categoryOption.EXPENSES) {
            grocerySection.hidden = true;
            expenseSection.hidden = false;
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
        } else if (selected == scheduleType.SCHEDULED || selected == scheduleType.MONTHLY) {
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
    let request = getXmlHttpRequest();

    request.onreadystatechange = function () {
        if (request.readyState == 4) {
            if (request.status == 200) {
                let json = request.responseText;
                let jarsDto = JSON.parse(json);
                let jarDtoList = jarsDto.jar_elements;

                // update the global list; list is empty
                for (let i = 0; i < jarDtoList.length; i++) {
                    let moneyJarDto = jarDtoList[i];
                    let jar = moneyJarDto.jar;

                    jars.setJar(jar.jar_id, moneyJarDto);
                }

                setJars("");

                let values = Object.values(jars.getJarList());
                drawPieChart(values);

                activateTimer();
            } else {
                console.log("Server could not find what you are looking for.")
            }
        }
    }

    let data = userFields.TOKEN + "=" + escape(window.localStorage.getItem("token")) + "&";
    data += userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));

    request.open("GET", ctx + "/dashboard/jars-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

function activateTimer() {
    let ids = Object.keys(jars.getJarList());
    ids.forEach(key => {
        let jarDto = jars.getJar(key);
        let jar = jarDto.jar;
        let label = jar.jar_label;
        (function(key, dayScheduled) {
            let today = new Date();
            let timeScheduled = new Date(dayScheduled);
            if (timeScheduled.getTime() <= today.getTime()) {
                showNotificationDialog(key, jarDto);
                return;
            }

            window.setTimeout(arguments.callee, 1000, dayScheduled);
        })(key, jar.scheduled_for);
    });
}

function drawPieChart(jarElements) {
    let jarMap = {};
    let count = 0;
    jarElements.forEach(jarElement => {
        jarMap[count] = jarElement.jar;
        count++;
    });

    let jarItems = Object.values(jarMap);
    let colors = ["#F3004D", "#4E4D4A", "#333333", "#74B100", "#353432", "#85C760", "#008FB4", "#F8FFE1", "#294875", "#AA002E"]
    let properties = {
        canvas: jarsCanvas,
        jars: jarItems,
        colors: colors
    }

    let pieChart = new PieChart(properties);
    pieChart.draw();
}

function openJarModal(callback) {
    jarModal.style.display = "block";

    btnSubmitJar.onclick = function() {
        updateJar(callback);
    }
}

function openJarModalForEdit(jarId) {

    moneyJarIdModal.value = jarId;
    
    jarModal.style.display = "block";

    let jarDto = jars.getJar(jarId);
    let jar = jarDto.jar;
    // Set inputs to be changed
    jarLabel.value = jar.jar_label;
    amountElem.innerHTML = jar.amount;
    let dateArr = jar.scheduled_for.split(" ");
    date.value = dateArr[0];
    time.value = dateArr[1];

    amountElem.innerHTML = jar.amount;

    if (jar.category == "Groceries") {
        categorySelector.options[0].selected = true;
        let groceries = jar.liabilities;

        groceryListSection.innerHTML = "";
        groceryListSection.appendChild(setGroceries(groceries));

        expenseSection.hidden = true;
        grocerySection.hidden = false;
    } else {
        categorySelector.options[1].selected = true;

        expenseItemsSection.innerHTML = "";
        expenseItemsSection.appendChild(setExpense(jarId));

        expenseSection.hidden = false;
        grocerySection.hidden = true;
    }

    if (jar.scheduled_type == "scheduled") {
        scheduleSelector.options[0].selected = true;
    } else if (jar.scheduled_type == "Daily") {
        scheduleSelector.options[1].selected = true;
    } else if (jar.scheduled_type == "Weekly") {
        scheduleSelector.options[2].selected = true;
        setDay(jar.scheduled_for);
    } else if (jar.scheduled_type == "Monthly") {
        scheduleSelector.options[3].selected = true;
    }

    btnSubmitJar.onclick = function() {
        updateMoneyJar(jarId);
    }
}

/**
 * select the day of the week for that schedule.
 * 
 * @param {string} day weekday: Monday, Tuesday, Wednesday, etc
 */
function setDay(day) {
    if (day == "Monday") {
        daySelector.options[0].selected = true;
    } else if (day == "Tuesday") {
        daySelector.options[1].selected = true;
    } else if (day == "Wednesday") {
        daySelector.options[2].selected = true;
    } else if (day == "Thursday") {
        daySelector.options[3].selected = true;
    } else if (day == "Friday") {
        daySelector.options[4].selected = true;
    } else if (day == "Saturday") {
        daySelector.options[5].selected = true;
    } else if (day == "Sunday") {
        daySelector.options[0].selected = true;
    } else {
        daySelector.options[0].selected = true;
    }
}

/**
 * sends a request throught the controller to add an item to grocery
 */
function sendJarRequest() {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                let moneyJarDto = JSON.parse(json);
                addJarToList(moneyJarDto);
            } else {
                showError();
            }
        }
    }

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/jars-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

function updateJar(callback) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                let moneyJarDto = JSON.parse(json);

                addJarToList(moneyJarDto);

                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len == 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
                
                jarModal.style.display = "none";
            } else {
                showError();
            }
        }
    }

    var data = serializeData();

    request.open("PUT", ctx + "/dashboard/jars-controller?" + data, true);
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
        if (request.readyState == 4) {
            if (request.status == 200) {
                let json = request.responseText;
                let moneyJarDto = JSON.parse(json);
                addJarToList(moneyJarDto);
            } else {
                showError();
            }
        }
    }

    let data = serializeData();

    request.open("PUT", ctx + "/dashboard/jars-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

/**
 * Add the item added by the user to aa global list variable
 * @param {Jars[moneyJar
 *Dto contains a list of jars with elements
 */
function addJarToList(moneyJarDto) {
    let jar = moneyJarDto.jar;
    // check size before adding item
    let beforeLen = Object.keys(jars.getJarList()).length;

    jars.setJar(jar.jar_id, jar);

    // check length after adding item
    let afterLen = Object.keys(jars.getJarList()).length;

    if (afterLen > beforeLen) {
        setJars(jar.jar_id);
    }

}

/**
 * Update the ui with data from server about jars
 * @param {Jar[]} jars all the jars from the server for this household.
 * @param {String} jarId id that identifies that added jar
 */
function setJars(jarId) {

    // let rows = moneyJarTbody.rows;
    let keys = Object.keys(jars.getJarList());
    if (jarId == "") {

        keys.forEach(key =>  {
            let jarDto = jars.getJar(key);
            let jar = jarDto.jar;
            updateJarTable(jar);
        });
    } else {
        
        let jarDto = jars.getJar(jarId);
        let jar = jarDto.jar;
        updateJarTable(jar);
    }
    // update the table with new item
}

/**
 * update the jar table with the name of the jar and scheduled date
 * @param {Jar} jar contains all items of the jar
 * @param {Integer} index represents the row to update
 */
function updateJarTable(jar) {

    let label = jarTemplate.content.querySelector(".jar-label");
    label.textContent = jar.jar_label;
    let interval = jarTemplate.content.querySelector(".scheduled-interval");
    interval.textContent = jar.scheduled_type;
    let date = jarTemplate.content.querySelector(".scheduled-date");
    date.textContent = jar.scheduled_for;
    let amount = jarTemplate.content.querySelector(".jar-amount");
    amount.textContent = jar.amount;

    let clone = document.importNode(jarTemplate.content, true);
    jarList.appendChild(clone);
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
    if (jarLabel.value == "") {
        jarModalError.innerHTML = "This field is required: Jar Name";
        return;
    }
    
    // get list depending on category
    if (selectedMoneyJarType == null) {
        selectedMoneyJarType = categorySelector.options[categorySelector.selectedIndex].value;
    }
    
    let items;
    if (selectedMoneyJarType == categoryOption.EXPENSES) {
        liabilitiesObj.expense = expense;
        items = JSON.stringify(liabilitiesObj);
    } else if (selectedMoneyJarType == categoryOption.GROCERIES) {
        groceryDto.groceries = Object.values(groceryListObj);
        items = JSON.stringify(groceryDto);
    }

    if (items == "" && selectedMoneyJarType == categoryOption.GROCERIES) {
        jarModalError.innerHTML = "Your grocery list is empty";
        return;
    }

    let data = userFields.TOKEN + "=" + token + "&";
    data += userFields.USER_ID + "=" + userId + "&";
    data += jarFields.JAR_ID + "=" + escape(moneyJarIdModal.value) + "&";
    data += jarFields.JAR_LABEL + "=" + escape(jarLabel.value) + "&";
    data += jarFields.CATEGORY + "=" + escape(selectedMoneyJarType) + "&";
    data += jarFields.TOTAL_AMOUNT + "=" + escape(amountElem.innerHTML) + "&";
    
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
    currentTotal = parseInt(amount) + parseInt(currentTotal);

    amountElem.innerHTML = currentTotal;
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