let user;
let income;
let household;

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

/**
 * Grocery table
 */
var groceryListTBody;
var groceryListObj = {};
var grocerySelected;
var isGroceryUpdate = false;

var modal;
var scheduleModal;
var incomeModal;
/**
 * Grocery Modal
 */
var groceryName;
var groceryDesc;
var groceryPrice;
var groceryRequired;
var groceryRemaining;

/**
 * Enumerations to represent folders in settings page
 */
const folders = {
    ROOT: 'root',
    INCOME: 'income',
    PREFERENCES: 'preferences'
}

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
    TOKEN: 'token'
}

const incomeFields = {
    INCOME_ID: 'income_id',
    ACCOUNT_TYPE: 'account_type',
    AMOUNT: 'amount',
    CREATED_AT: 'created_at',
}

const requestHeader = {
    CONTENT_TYPE: 'Content-Type'
}

const mediaType = {
    APPLICATION_JSON: 'application/json',
    FORM_ENCODED: 'application/x-www-form-urlencoded'
}

const requestMethod = {
    PUT: 'PUT',
    GET: 'GET',
    UPDATE: 'UPDATE',
    POST: 'POST',
    DELETE: 'DELETE'
}

function loadWindow() {
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
    }

    document.getElementById("members").onclick = function() {
        getPage("members-title");
    }
    
    document.getElementById("home").onclick = function() {
        getPage("home-title");
    }

    document.getElementById("option-logout").onclick = function() {
        logout();
    }

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
    }

    document.getElementById("settings-item").onclick = function() {
        getPage("settings-option");
    }

    modal = document.getElementById("groceryModal");
    scheduleModal = document.getElementById("scheduleModal");
    incomeModal = document.getElementById("incomeModal");

    // initialize modal variables
    groceryName = document.getElementById("groceryName");
    groceryDesc = document.getElementById("groceryDesc");
    groceryPrice = document.getElementById("itemPrice");
    groceryRequired = document.getElementById("requireQuantity");
    groceryRemaining = document.getElementById("remainingQuantity");
    
    var btnOpenGroceryModal = document.getElementById("btnOpenGroceryModal");
    btnOpenGroceryModal.onclick = function() {
        modal.style.display = "block";
        groceryDesc.value = "";
        groceryName.value = "";
        groceryPrice.value = "";
        groceryRequired.value = "";
        groceryRemaining.value = "";
    }

    var btnCloseGroceryModal = document.getElementById("cancelGroceryModal");
    btnCloseGroceryModal.onclick = function() {
        modal.style.display = "none";
    }

    var btnAddGrocery = document.getElementById("addItem");
    btnAddGrocery.onclick = function() {
        sendRequestGrocery(modal);
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        } else if (event.target == scheduleModal) {
            scheduleModal.style.display = "none";
        } else if (event.target == incomeModal) {
            incomeModal.style.display = "none";
        }
    }

    getUserDetails();
    
    // get members when window loads
    getMembers();
    
    // get grocery list
    groceryListTBody = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];
    getGroceries();

    // configuration when window loads
    configureSettings();
    configureSchedule();
    configureIncomeElements();
}

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
        if (request.readyState == 4) {
            if (request.status == 200) {
                var obj = JSON.parse(request.responseText);
                user = obj.user;
                income = obj.income;
                household = obj.household;
                
                var usernameEle = document.getElementById("username");
                usernameEle.innerHTML = user.username;

                showIncome(income);
            }
        }
    }

    var username = userFields.USERNAME + "=" + escape(window.localStorage.getItem(userFields.USERNAME));
    var token = userFields.TOKEN + "=" + escape(window.localStorage.getItem("token"));
    var data = username + "&" + token;

    request.open("GET", ctx + "/dashboard/user-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

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

    if (getLength(hours) == 1) {
        hours = "0" + hours;
    }

    if (getLength(min) == 1) {
        min = "0" + min;
    }

    if (getLength(seconds) == 1) {
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

/**
 * sends a request throught the controller to add an item to grocery
 */
function sendRequestGrocery(modal) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                var obj = JSON.parse(json);
                groceryListObj[obj.grocery_id] = obj;
                var item = groceryListObj[obj.grocery_id];

                // get index of new/old entry
                var index = getGroceryItemIndex(item.grocery_id);

                // get number of rows
                var row;

                if (isGroceryUpdate) {
                    row = groceryListTBody.rows[index];
                    var cells = row.cells;
                    cells[0].innerHTML = item.grocery_name;
                    cells[1].innerHTML = item.grocery_price;
                    cells[2].innerHTML = item.remaining_quantity;
                    cells[3].innerHTML = item.required_quantity;
                    isGroceryUpdate = false;
                } else {
                    row = groceryListTBody.insertRow(index);
                    var name = row.insertCell(0);
                    var price = row.insertCell(1);
                    var quantity = row.insertCell(2);
                    var required = row.insertCell(3);

                    name.innerHTML = item.grocery_name;
                    price.innerHTML = item.grocery_price;
                    quantity.innerHTML = item.remaining_quantity;
                    required.innerHTML = item.required_quantity;
                }

                row.onclick = (function() {
                    return function() {
                        onItemClick(obj.grocery_id);
                    }
                })();

                if (index%2 == 0) {
                    row.style.backgroundColor = "#534c63d2";
                }
                row.style.cursor = "pointer";

                modal.style.display = "none";

                grocerySelected = null;
            } else {
                var error = document.getElementById("grocery-modal-error");
                error.innerHTML = "An Error occured please try again.";
                error.hidden = false;
            }
        }
    }

    var userId = "user_id=" + escape(window.localStorage.getItem("user_id"));
    var token = "token=" + escape(window.localStorage.getItem("token"));
    var username = "username=" + escape(window.localStorage.getItem("username"));

    // serialize grocery items details
    var id = grocerySelected == null ? "" : grocerySelected.grocery_id;
    var name = groceryName.value;
    var desc = groceryDesc.value;
    var price = groceryPrice.value;
    var required = groceryRequired.value;
    var remaining = groceryRemaining.value;

    if (name == "" || desc == "" || price == "" || required == "" || remaining == "") {

        var error = document.getElementById("grocery-modal-error");
        error.innerHTML = "All fields are required!";
        error.hidden = false;
        return;
    }

    id = "grocery_id=" + escape(id);
    remaining = "remaining_quantity=" + escape(remaining);
    required = "required_quantity=" + escape(required);
    price = "grocery_price=" + escape(price);
    desc = "grocery_description=" + escape(desc);
    name = "grocery_name=" + escape(name);

    var data = userId + "&";
    data += token + "&";
    data += username + "&";
    data += name + "&";
    data += desc + "&";
    data += price + "&";
    data += required + "&";
    data += remaining + "&";
    data += id;


    request.open("PUT", ctx + "/dashboard/groceries-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

function getGroceryItemIndex(sample) {
    var count = 0;
    var keys = Object.keys(groceryListObj);
    for (let i = 0; i < keys.length; i++) {
        var key = keys[i];
        if (sample == key) {
            break;
        } else {
            count++;
        }
    }

    return count;
}

function getGroceries() {
    var userId = window.localStorage.getItem("user_id");
    var token = window.localStorage.getItem("token");
    var username = window.localStorage.getItem("username");

    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                var json = request.responseText;
                var obj = JSON.parse(json);
                var items = obj.groceries;

                var body = document.getElementById("groceryItems").getElementsByTagName("tbody")[0];
                for (let i = 0; i < items.length; i++) {
                    // get number of rows
                    var row = body.insertRow(i);
                    if (i%2 == 0 || i == 0) {
                        row.style.backgroundColor = "#534c63d2";
                    }
                    row.style.cursor = "pointer";

                    row.onclick = (function() {
                        var currentIndex = i;
                        return function() {
                            onItemClick(items[currentIndex].grocery_id);
                        }
                    })();
                    
                    var name = row.insertCell(0);
                    var price = row.insertCell(1);
                    var quantity = row.insertCell(2);
                    var required = row.insertCell(3);
                    
                    name.innerHTML = items[i].grocery_name;
                    price.innerHTML = items[i].grocery_price;
                    quantity.innerHTML = items[i].remaining_quantity;
                    required.innerHTML = items[i].required_quantity;

                    groceryListObj[items[i].grocery_id] = items[i];
                }
            } else {
                var error = document.getElementById("grocery-modal-error");
                error.innerHTML = "An Error occured please try again.";
                error.hidden = false;
                console.log("Error status: " + request.status);
            }
        }
    }

    userId = "user_id=" + escape(userId);
    username = "username=" + escape(username);
    token = "token=" + escape(token);
    var data = userId + "&" + username + "&" + token;

    request.open("GET", ctx + "/dashboard/groceries-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}

/**
 * defines a function triggered when the user selects an item in the grocery list.
 * @param {integer} row defines the position of the selected row
 */
function onItemClick(row) {
    modal.style.display = "block";
    
    grocerySelected = groceryListObj[row];
    var name = grocerySelected.grocery_name;
    var desc = grocerySelected.grocery_description;
    var price = grocerySelected.grocery_price;
    var required = grocerySelected.required_quantity;
    var remaining = grocerySelected.remaining_quantity;

    groceryName.value = name;
    groceryDesc.value = desc;
    groceryPrice.value = price;
    groceryRequired.value = required;
    groceryRemaining.value = remaining;

    isGroceryUpdate = true;
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
        if (request.readyState == 4) {
            if (request.status == 200) {
                var obj = JSON.parse(request.responseText);

                //
                if (obj.title != "") {
                    var lastKey = getLastHistoryKey();
                    window.history.pushState(obj.title, obj.title, obj.url);
                    urlMap[obj.title] = obj.url;
                    document.getElementById(lastKey == "" ? "mainContent" : lastKey).hidden = true;
                    document.getElementById(obj.title).hidden = false;
                } else {
                    var lastKey = getLastHistoryKey();
                    document.getElementById(lastKey == "" ? "mainContent" : lastKey).hidden = false;
                    if (!document.getElementById("mainContent").hidden) {
                        document.getElementById("mainContent").hidden = true;
                    }
                }
            }
        }
    }
    
    var key = id == "refresh" ? id : document.getElementById(id).value;
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
        if (title == key) {
            isKeyFound = true;
        }
    });
    return isKeyFound;
}

/**
 * get key then pop concequitive elements in history
 */
function getUrl(title) {
    var keys = Object.keys(urlMap);
    var count = keys.length - 1;
    var url = "";
    for (let i = count; i >= 0; i--) {
        const key = keys[i];
        if (title == key) {
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
}

function removeKeyFromMap(lastKey) {
    Object.keys(urlMap).forEach(function(key) {
        if (key == lastKey) {
            delete urlMap[lastKey];
        }
    });
}

function getLastHistoryKey() {
    var count = 0;
    var size = Object.keys(urlMap).length;
    var lastKey = "";
    Object.keys(urlMap).forEach( function(key) {
        if (count == (size - 1) ) {
            lastKey = key;
        }
        count += 1;
    });

    return lastKey;
}

window.onbeforeunload = function() {
    saveHistory();
}

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
        if (count == (size - 1) ) {
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
    document.cookie = name + "=" + history + ((dateExpires == null) ? "" : "; expires=" + dateExpires.toGMTString());
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
        if (items.length ==  2) {
            key = items[0]
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
            if (count == (size - 1) ) {
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
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) endstr = document.cookie.length;
    return unescape(document.cookie.substring(offset, endstr));
}

function updateBreadcrumbs() {
    var history = "";
}

function getMembers() {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status != 200) {
                
            } else {

            }
        }
    }
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
        menuTitles[i].style = "display: none;"
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
        menuTitles[i].style = "display: inline;"
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