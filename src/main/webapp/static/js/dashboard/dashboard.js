let user;
let income;
let budget;
let householdArr;
let userHouseholdArr;
let accountStatus;
let householdMembers;

const Status = {
    COMPLETE: 'Complete',
    PARTIAL: 'Partial'
}

var timeInterval = setInterval(function() {myTimer()}, 1000);
var navClickOpen = false;
var ctx;
var urlMap = {};
var cookieName = "historyLocation";
var daysToExpire = 365;
var countBackPress = 0;
var isOptionsMenuOpen = false;

// Elements
var optionsMenuItems;
var optionsMenu;

var scheduleModal;
var incomeModal;
var householdModal;
var jarModal;
var groceryModal;
var expensesModal;
let budgetModal;

/**
 * Required settings in account
 */
var incomplete = {};
var complete = {};
var scheduled = {};

/**
 * Enumerations to represent folders in settings page
 */
const folders = {
    ROOT: 'root',
    INCOME: 'income',
    PREFERENCES: 'preferences'
};

/**
 * Fields of known values used in app
 */
const userFields = {
    USER_ID: 'user_id',
    USERNAME: 'username',
    EMAIL: 'email',
    PASSWORD: 'password',
    IS_ADMIN: 'is_admin',
    INCOME: 'income',
    TOKEN: 'token',
    PHONE_NUMBER: 'mob_number'
};

const incomeFields = {
    INCOME_ID: 'income_id',
    ACCOUNT_TYPE: 'account_type',
    AMOUNT: 'amount',
    CREATED_AT: 'created_at'
};

const requestHeader = {
    CONTENT_TYPE: 'Content-Type',
    AUTHORIZATION: 'Authorization'
};

const mediaType = {
    APPLICATION_JSON: 'application/json',
    FORM_ENCODED: 'application/x-www-form-urlencoded'
};

const requestMethod = {
    PUT: 'PUT',
    GET: 'GET',
    UPDATE: 'UPDATE',
    POST: 'POST',
    DELETE: 'DELETE'
};

const accountStatusField = {
    HOUSEHOLD: 'household_status',
    EXPENSES: 'expenses_status',
    MONEY_JAR: 'jar_status',
    INCOME: 'income_status',
    ACCOUNT: 'account_status',
    BUDGET: 'budget_status'
};

/**
 * Serializeable names for expense JSON Object
 */
const expenseFields = {
    EXPENSE_ID: "expense_id",
    EXPENSE_NAME: "expense_name",
    EXPENSE_DESCRIPTION: "expense_description",
    AMOUNT: "amount",
    PAYEE_NAME: "payee_name",
    BUSINESS_NUMBER: "business_number",
    ACCOUNT_NUMBER: "account_number"
};

/**
 * Serializeable fields for Money Jar JSON object
 */
const jarFields = {
    JAR_ID: "jar_id",
    EXPENSE_TYPE: "expense_type",
    CATEGORY: "category",
    TOTAL_AMOUNT: "amount",
    LIABILITIES: "liabilities",
    SCHEDULE: "scheduled_for",
    SCHEDULED_TYPE: "scheduled_type"
};

window.onload = function() {
    
    // use this when the page is reloaded to obtain the recent visited page
    loadHistoryLocation();

    // get context path
    ctx = document.getElementById("contextPath").value;

    urlMap["mainContent"] = ctx + "/dashboard";
    var navigationDrawer = document.getElementById("navigationDrawer");
    var menuTitles = document.getElementsByClassName("menu-title");
    var statusAvatar = document.getElementById("statusIndicatorAvatar");
    var name = document.getElementById("userName");
    var status = document.getElementById("status");
    var statusDetails = document.getElementById("statusIndicator");
    var details = document.getElementById("details");
    
    // document.getElementById("navIcon").onmouseover = function() {
    //     if (!navClickOpen) {
    //         openNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);
    //     }
    // };

    // navigationDrawer.onmouseout = function() {
    //     if (!navClickOpen) {
    //         closeNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);
    //     }
    // };

    document.getElementById("settings").onclick = function() {
        getPage("settings-title:root");
    };

    document.getElementById("members").onclick = function() {
        getPage("members-title");
    };
    
    document.getElementById("home").onclick = function() {
        getPage("home-title");
    };

    document.getElementById("option-logout").onclick = function() {
        logout();
    };

    optionsMenu = document.getElementById("optionsMenu");
    optionsMenuItems = document.getElementsByClassName("option-menu-item");

    document.getElementById("avatarContainer").onclick = function() {
        if (!isOptionsMenuOpen) {
            openOptionsMenu();
            isOptionsMenuOpen = true;
        } else {
            closeOptionsMenu();
            isOptionsMenuOpen = false;
        }
    };

    document.getElementById("settings-item").onclick = function() {
        getPage("settings-option");
    };

    groceryModal = document.getElementById("groceryModal");
    scheduleModal = document.getElementById("scheduleModal");
    incomeModal = document.getElementById("incomeModal");
    householdModal = document.getElementById("householdModal");
    jarModal = document.getElementById("jarModal");
    expensesModal = document.getElementById("expensesModal");
    budgetModal = document.getElementById("budgetModal");

    window.onclick = function(event) {
        if (event.target === groceryModal) {
            groceryModal.style.display = "none";
        } else if (event.target === scheduleModal) {
            scheduleModal.style.display = "none";
        } else if (event.target === incomeModal) {
            incomeModal.style.display = "none";
        } else if (event.target === jarModal) {
            jarModal.style.display = "none";
        } else if (event.target === expensesModal) {
            expensesModal.style.display = "none";
        } else if (event.target === budgetModal) {
            budgetModal.style.display = "none";
        }
    };

    getUserDetails();

    // configuration when window loads
    configureSettings();
    // configureSchedule();
    configureIncomeElements();
    configureGrocery();
    configureHousehold();
    configureMoneyJar();
    configureExpenses();
    configureMembers();
    configurePayments();
//    configureBudget();
};

//window.setInterval(function() {
//    getUserDetails();
//    getAllMoneyJars();
//}, 5000);

/**
 * set income when reeived.
 * 
 * @param {income} obj rep the income values
 */
function setIncome(obj) {
    income = obj;
    user.income = income.amount;
}

/**
 * Get user details from backend
 */
function getUserDetails() {
    var request = getXmlHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                var obj = JSON.parse(request.responseText);
                user = obj.user;
                income = obj.income;
                budget = obj.budget;
                
                householdArr = obj.households;
                userHouseholdArr = obj.relations;
                accountStatus = obj.account_status_update;
                setHouseholdId();

                // show members
                householdMembers = obj.members;
                setMembers();

                // create a list of not complete settings
                var status = {};
                status[accountStatusField.INCOME] = accountStatus.income_status;
                status[accountStatusField.MONEY_JAR] = accountStatus.jar_status;
                status[accountStatusField.HOUSEHOLD] = accountStatus.household_status;

                openNotCompleteModals(status);
                
                var usernameEle = document.getElementById("username");
                usernameEle.innerHTML = user.username;

                showIncome(income);

//                showBudgetAmount(budget);
            }
        }
    };

    var userId = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID));
    var token = userFields.TOKEN + "=" + escape(window.localStorage.getItem("token"));
    var data = userId + "&" + token;

    request.open("GET", ctx + "/dashboard/user-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

/**
 * Open Modals whose settings have not been completed.
 * @param {Map} statusMap holds all account settings updates
 */
function openNotCompleteModals(statusMap) {
    var keys = Object.keys(statusMap);

    // Loop through the hashmap checking date and status
    for (let i = 0; i < keys.length; i++) {
        let key = keys[i];
        if (statusMap[key] !== undefined) {
            var obj = JSON.parse(statusMap[key]);
            var status = obj.status;
            var dateStr = obj.date;
    
            var now = new Date();
            // check that status is not complete or date set
            if (dateStr !== "" && status !== Status.COMPLETE) {
                if (now > new Date(dateStr)) {
                    if (key === accountStatusField.INCOME) {
                        incomplete[key] = incomeModal;
                    } else if (key === accountStatusField.MONEY_JAR) {
                        incomplete[key] = jarModal;
                    } else if(key === accountStatusField.BUDGET){
                        incomplete[key] = budgetModal;
                    } else if (key === accountStatusField.HOUSEHOLD) {
                        incomplete[key] = householdModal;
                    }
                } else if (now < new Date(dateStr)) {
                    if (key === accountStatusField.INCOME) {
                        scheduled[key] = incomeModal;
                    } else if (key === accountStatusField.MONEY_JAR) {
                        scheduled[key] = jarModal;
                    }  else if(key === accountStatusField.BUDGET){
                        incomplete[key] = budgetModal;
                    } else if (key === accountStatusField.HOUSEHOLD) {
                        scheduled[key] = householdModal;
                    }
                }
            }
        } else {
            if (key === accountStatusField.INCOME) {
                incomplete[key] = incomeModal;
            } else if (key === accountStatusField.MONEY_JAR) {
                incomplete[key] = jarModal;
            } else if(key === accountStatusField.BUDGET){
                incomplete[key] = budgetModal;
            } else  if (key === accountStatusField.HOUSEHOLD) {
                incomplete[key] = householdModal;
            }
        }
    }

    // call a function with a callback
    var len = Object.keys(incomplete).length;
    if (len > 1) {
        var modalKeys = Object.keys(incomplete);

        openIncompleteModals(modalKeys);
    } else if (len === 1) {
        modalKeys = Object.keys(incomplete);
        openIncompleteModals(modalKeys);
    }
}

function openIncompleteModals(modalKeys) {
    openModal({
        key: modalKeys[0],
        button: "Next",
        isLastModal: false,
        onNext: function(callback) {
            modalKeys = Object.keys(incomplete);
            callback.key = modalKeys[0];
            callback.button = "Next";
            openModal(callback);
        },
        onDone: function(callback) {
            modalKeys = Object.keys(incomplete);
            callback.key = modalKeys[0];
            callback.button = "Submit";
            openModal(callback);
        },
        onComplete: function() {
            console.log("You have completed updating your account.");
        }
    });
}

window.openModal = function(modal) {
    if (modal.key === accountStatusField.INCOME) {
        openIncomeModal(modal);
    } else if (modal.key === accountStatusField.MONEY_JAR) {
        openJarModal(modal);
    } else if (modal.key === accountStatusField.HOUSEHOLD) {
        openHouseholdModal(modal);
    }
};

function getXmlHttpRequest() {
    var request;
    if (window.XMLHttpRequest) {
        request = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        request = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
        request = null;
    }
    return request;
}

function myTimer() {
    var date = new Date();
    var hours = "" + date.getHours();
    var min = "" + date.getMinutes();
    var seconds = "" + date.getSeconds();

    if (getLength(hours) === 1) {
        hours = "0" + hours;
    }

    if (getLength(min) === 1) {
        min = "0" + min;
    }

    if (getLength(seconds) === 1) {
        seconds = "0" + seconds;
    }

    var time = hours + ":" + min + ":" + seconds;
    document.getElementById("timeNow").innerHTML = time;
}

/**
 * returns the size of the string literal.
 * @param {string} s string literal to find the length/number of characters
 */
function getLength(s) {
    return [...s].length;
}

function openOptionsMenu() {
    
    optionsMenu.style.display = "block";
    optionsMenu.classList.add("open-options");
    optionsMenu.classList.remove("close-options");

    for (let i = 0; i < optionsMenuItems.length; i++) {
        const item = optionsMenuItems[i];
        item.style.width = "80px";
        item.style.height = "inherit";
        item.style.display = "block";
    }
}

function closeOptionsMenu() {
    optionsMenu.style.display = "none";
    optionsMenu.classList.remove("open-options");
    optionsMenu.classList.add("close-options");

    for (let i = 0; i < optionsMenuItems.length; i++) {
        const item = optionsMenuItems[i];
        item.style.width = "0";
        item.style.height = "0";
        item.style.display = "none";
    }
}

/**
 * gets the page to display/server
 * 
 * @param {string} id menu item type eg. 'refresh', 'menu-title', 'menu-option' the literal is in the form type:folder
 * Where type = the page to serve & folder = the subsection of the page.
 */
function getPage(id) {
    var arrStr = id.split(":");
    var folder = arrStr[1];
    id = arrStr[0];
    // if option menu item clicked close option menu
    if (id.match("^.*option")) {
        closeOptionsMenu();
        isOptionsMenuOpen = false;
    }
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                var obj = JSON.parse(request.responseText);

                //
                if (obj.title !== "") {
                    var lastKey = getLastHistoryKey();
                    window.history.pushState(obj.title, obj.title, obj.url);
                    urlMap[obj.title] = obj.url;
                    document.getElementById(lastKey === "" ? "mainContent" : lastKey).hidden = true;
                    document.getElementById(obj.title).hidden = false;
                } else {
                    var lastKey = getLastHistoryKey();
                    document.getElementById(lastKey === "" ? "mainContent" : lastKey).hidden = false;
                    if (!document.getElementById("mainContent").hidden) {
                        document.getElementById("mainContent").hidden = true;
                    }
                }
            }
        }
    };
    
    var key = id === "refresh" ? id : document.getElementById(id).value;
    if (isTitleInUrlMap(key)) {
        var url = getUrl(key);
        window.history.pushState(key, key, url);
        document.getElementById(key).hidden = false;
    } else {
        var title = "title=" + escape(key);
        var data = title;

        request.open("GET", ctx + "/dashboard/controller?" + data, true);
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        request.send();
    }
}

/**
 * return true is key is in map false otherwise
 * @param {key} urlKey Key in urlMap
 */
function isTitleInUrlMap(urlKey) {
    var title = urlKey;
    var isKeyFound = false;
    Object.keys(urlMap).forEach(function(key) {
        if (title === key) {
            isKeyFound = true;
        }
    });
    return isKeyFound;
}

/**
 * get key then pop concequitive elements in history
 * @param {string} title folder url
 */
function getUrl(title) {
    var keys = Object.keys(urlMap);
    var count = keys.length - 1;
    var url = "";
    for (let i = count; i >= 0; i--) {
        const key = keys[i];
        if (title === key) {
            url = urlMap[key];
            break;
        } else {
            delete urlMap[key];
        }
    }
    return url;
}

/**
 * Handle on back button pressed
 */
window.onpopstate = function(e) {
    var lastKey = Object.keys(urlMap).pop();
    removeKeyFromMap(lastKey);
    document.getElementById(e.state.id).hidden = true;

    var lastKeyInHistory = getLastHistoryKey();
    document.getElementById(lastKeyInHistory).hidden = false;
};

function removeKeyFromMap(lastKey) {
    Object.keys(urlMap).forEach(function(key) {
        if (key === lastKey) {
            delete urlMap[lastKey];
        }
    });
}

function getLastHistoryKey() {
    var count = 0;
    var size = Object.keys(urlMap).length;
    var lastKey = "";
    Object.keys(urlMap).forEach( function(key) {
        if (count === (size - 1) ) {
            lastKey = key;
        }
        count += 1;
    });

    return lastKey;
}

window.onbeforeunload = function() {
    saveHistory();
};

/**
 * Save history of visited location in cookie
 * format "key:location"
 */
function saveHistory() {
    var dateExpires =  new Date();
    dateExpires.setTime(dateExpires.getTime() + (60*10*1000));

    var count = 0;
    var location = "";
    var size = Object.keys(urlMap).length;
    var lastLocation = "";
    var lastKey = "";
    Object.keys(urlMap).forEach( function(key) {
        if (count === (size - 1) ) {
            location += key + ":" + urlMap[key];
            lastLocation = urlMap[key];
            lastKey = key;
        } else {
            location += key + ":" + urlMap[key] + ">";
        }
        count += 1;
    });
    saveCookie(cookieName, dateExpires, location);
}

function saveCookie(name, dateExpires, history) {
    document.cookie = name + "=" + history + ((dateExpires === undefined) ? "" : "; expires=" + dateExpires.toGMTString());
}

function loadHistoryLocation() {
    var history = getCookie(cookieName);
    if (!history) {
        return;
    }

    var locations = history.split(">");
    for (let i = 0; i < locations.length; i++) {
        var location = locations[i];

        // get key value pairs
        var items = location.split(":");
        var key = "";
        var value = "";
        if (items.length ===  2) {
            key = items[0];
            value = items[1];
        }

        // save the history into urlMap
        urlMap[key] = value;
    }

    document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC";

    if (Object.keys(urlMap).length > 1) {
        var count = 0;
        var size = Object.keys(urlMap).length;
        Object.keys(urlMap).forEach( function(key) {
            if (count === (size - 1) ) {
                if (document.getElementById(key).hidden) {
                    window.history.pushState(key, key, value);
                    document.getElementById(key).hidden = false;
                    if (!document.getElementById("mainContent").hidden) {
                        document.getElementById("mainContent").hidden = true;
                    }
                }
            }
            count += 1;
        });
    }
}

function getCookie(name) {
    var arr = document.cookie.split(";");

    for (let i = 0; i < arr.length; i++) {
        var attr = arr[i].split("=");
        if (attr[0].match(name)) {
            return attr[1];
        }
    }
    return null;
}

function getCookieVal(offSet) {
    var endstr = document.cookie.indexOf(";", offSet);
    if (endstr === -1) endstr = document.cookie.length;
    return unescape(document.cookie.substring(offSet, endstr));
}

function updateBreadcrumbs() {
    var history = "";
}

function openOrCloseNav() {

    var navigationDrawer = document.getElementById("navigationDrawer");
    var icon = document.getElementById("navBarIcon");
    var menuTitles = document.getElementsByClassName("menu-title");
    var statusAvatar = document.getElementById("statusIndicatorAvatar");
    var name = document.getElementById("userName");
    var status = document.getElementById("status");
    var statusDetails = document.getElementById("statusIndicator");
    var details = document.getElementById("details");

    if (navClickOpen) {

        icon.classList.remove("back-btn-translation-open");
        icon.classList.add("back-btn-translation-close");
        icon.src = ctx + "/static/images/nav_menu_bar.png";

        navClickOpen = closeNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);

        document.getElementById("navIcon").style = "width: fit-content;";
    } else {

        icon.src = ctx + "/static/images/right_arrow.png";

        icon.classList.add("back-btn-translation-open");
        icon.classList.remove("back-btn-translation-close");
        
        navClickOpen = openNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);

        document.getElementById("navIcon").style = "width: 100%;";
    }
}

function closeNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details) {
    navigationDrawer.classList.remove("animate-nav-open");
    navigationDrawer.classList.add("animate-nav-close");

    var i;
    for (i = 0; i < menuTitles.length; i++) {
        menuTitles[i].classList.add("fade");
        menuTitles[i].classList.remove("unFade");
        menuTitles[i].style = "display: none;";
    }

    statusAvatar.hidden = false;
    name.classList.remove("unFade");
    name.classList.add("fade");
    name.hidden = true;

    status.classList.remove("unFade");
    status.classList.add("fade");
    status.hidden = true;

    statusDetails.hidden = true;

    details.style.backgroundImage = "url(\"\")";
    
    return false;
}

function openNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details) {
    navigationDrawer.classList.add("animate-nav-open");
    navigationDrawer.classList.remove("animate-nav-close");

    var i;
    for (i = 0; i < menuTitles.length; i++) {
        menuTitles[i].classList.remove("fade");
        menuTitles[i].classList.add("unFade");
        menuTitles[i].style = "display: inline;";
    }

    statusAvatar.hidden = true;
    name.classList.add("unFade");
    name.classList.remove("fade");
    name.hidden = false;
    
    status.classList.add("unFade");
    status.classList.remove("fade");
    status.hidden = false;

    statusDetails.hidden = false;

    details.style.backgroundImage = "url(" + ctx + "/static/images/avatar_bg.png)";

    return true;
}