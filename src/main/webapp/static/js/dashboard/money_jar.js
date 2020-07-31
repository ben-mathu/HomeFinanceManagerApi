var btnOpenJarModal;
var cancelJarModal;
var btnSubmitJar;

var categorySelector;
let scheduleSelector;
let daySelector;
let timePicker;
let datePicker;

let dayOfWeek;

let jars = {
    moneyJarList: {},
    moneyJarListListener: function(val) {},
    setJar: function(id, val) {
        this.moneyJarList[id] = val;
        this.moneyJarListListener(val);
    },
    getJar: function(val) {
        return this.moneyJarList[val];
    },
    getJarList() {
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

function configureMoneyJar() {
    
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
    let grocerySection = document.getElementById("groceries");
    // Jar sections
    let jarSection = document.getElementById("expense");

    categorySelector = document.getElementById("categorySelector");
    categorySelector.addEventListener("change", function () {
        selectedMoneyJarType = categorySelector.options[categorySelector.selectedIndex].value;
        if (selectedMoneyJarType == categoryOption.GROCERIES) {
            grocerySection.hidden = false;
            jarSection.hidden = true;
        } else if (selectedMoneyJarType == categoryOption.EXPENSES) {
            grocerySection.hidden = true;
            jarSection.hidden = false;
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

                    jars.setJar(jar.jar_id, jar);
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
        let jar = jars.getJar(key);
        (function(dayScheduled) {
            if (new Date(dayScheduled).getTime() <= new Date().getTime()) {
                showNotificationDialog(jar);
                return;
            }

            window.setTimeout(arguments.callee, 1000, dayScheduled);
        })(jar.scheduled_for);
    });
}

function showNotificationDialog(jar) {
    let nameTitle = jar.jar_label;
    let descriptionBody = jar.description;

    let paymentDueDialog = document.getElementById("paymentDueDialog");
    paymentDueDialog.hidden = false;
}

function drawPieChart(jars) {
    let colors = ["#F3004D", "#4E4D4A", "#333333", "#74B100", "#353432", "#85C760", "#008FB4", "#F8FFE1", "#294875", "#AA002E"]
    let properties = {
        canvas: jarsCanvas,
        jars: jars,
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
            let jar = jars.getJar(key);
            updateJarTable(jar);
        });
    } else {
        
        let jar = jars.getJar(jarId);
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
    var userId = escape(window.localStorage.getItem("user_id"));
    var token = escape(window.localStorage.getItem("token"));

    // get elements in
    let jarLabel = document.getElementById("jarLabel");
    let amount = document.getElementById("totalAmount");
    let time = document.getElementById("scheduledHour");
    let date = document.getElementById("scheduledDate");
    let liabilitiesObj = {};

    var jarModalError = document.getElementById("jarModalError");
    if (jarLabel.value == "") {
        jarModalError.innerHTML = "This field is required: Jar Name";
        return;
    }
    
    // get list depending on category
    let items;
    if (selectedMoneyJarType == categoryOption.EXPENSES) {
        liabilitiesObj.expense = expense;
        items = JSON.stringify(liabilitiesObj);
    } else {
        groceryDto.groceries = Object.values(groceryListObj);
        items = JSON.stringify(groceryDto);
    }

    if (selectedMoneyJarType == null) {
        selectedMoneyJarType = categorySelector.options[categorySelector.selectedIndex].value;
    }

    if (items == "" && selectedMoneyJarType == categoryOption.GROCERIES) {
        jarModalError.innerHTML = "Your grocery list is empty";
        return;
    }

    let data = userFields.TOKEN + "=" + token + "&";
    data += userFields.USER_ID + "=" + userId + "&";
    data += jarFields.JAR_LABEL + "=" + escape(jarLabel.value) + "&";
    data += jarFields.CATEGORY + "=" + escape(selectedMoneyJarType) + "&";
    data += jarFields.TOTAL_AMOUNT + "=" + escape(amount.innerHTML) + "&";
    
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
 * Update moneyJar
 *Dto's total amount
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