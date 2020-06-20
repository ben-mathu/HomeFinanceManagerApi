var timeInterval = setInterval(function() {myTimer()}, 1000);
var navClickOpen = false;
var ctx = "";
var urlMap = {};
var cookieName = "historyLocation";
var daysToExpire = 365;
var countBackPress = 0;
var isOptionsMenuOpen = false;

// Elements
var optionsMenuItems;
var optionsMenu;

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

function getLength(s) {
    return [...s].length;
}

function loadWindow() {
    // use this when the page is reloaded to obtain the recent visited page
    loadHistoryLocation();

    // get context path
    cxt = document.getElementById("contextPath").value;

    urlMap["mainContent"] = cxt + "/dashboard";
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
        getPage("settings-title");
    }

    document.getElementById("members").onclick = function() {
        getPage("members-title");
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

    configureSettings();
    // get members when window loads
    getMembers();
}

function openOptionsMenu() {
    
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
 * @param {*} id menu item type eg. 'refresh', 'menu-title', 'menu-option'
 */
function getPage(id) {
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
    
    var title = "title=" + escape(id == "refresh" ? id : document.getElementById(id).value);
    var data = title;

    var path = document.getElementById("contextPath").value;
    request.open("GET", path + "/dashboard/controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
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

    urlMap.clear();
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
    ctx = document.getElementById("contextPath").value;

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
    ctx = document.getElementById("contextPath").value;
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