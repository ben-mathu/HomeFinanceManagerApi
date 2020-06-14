var timeInterval = setInterval(function() {myTimer()}, 1000);
var navClickOpen = false;
var ctx = "";

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

window.onload = function() {
    var navigationDrawer = document.getElementById("navigationDrawer");
    var menuTitles = document.getElementsByClassName("menu-title");
    var statusAvatar = document.getElementById("statusIndicatorAvatar");
    var name = document.getElementById("userName");
    var status = document.getElementById("status");
    var statusDetails = document.getElementById("statusIndicator");
    var details = document.getElementById("details");
    
    document.getElementById("navIcon").onmouseover = function() {
        if (!navClickOpen) {
            openNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);
        }
    };

    document.getElementById("navIcon").onmouseout = function() {
        if (!navClickOpen) {
            closeNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);
        }
    };

    // get members when window loads
    getMembers();
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
        icon.src = ctx + "static/images/nav_menu_bar.png";

        navClickOpen = closeNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details);

        document.getElementById("navIcon").style = "width: fit-content;";
    } else {

        icon.src = ctx + "static/images/right_arrow.png";

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
    }

    statusAvatar.hidden = false;
    name.classList.remove("unFade");
    name.classList.add("fade");
    name.hidden = true;

    status.classList.remove("unFade");
    status.classList.add("fade");
    status.hidden = true;

    statusDetails.hidden = true;

    details.style = "background-image: url(\"\")";
    
    return false;
}

function openNavDrawer(navigationDrawer, menuTitles, statusAvatar, name, status, statusDetails, details) {
    navigationDrawer.classList.add("animate-nav-open");
    navigationDrawer.classList.remove("animate-nav-close");

    var i;
    for (i = 0; i < menuTitles.length; i++) {
        menuTitles[i].classList.remove("fade");
        menuTitles[i].classList.add("unFade");
    }

    statusAvatar.hidden = true;
    name.classList.add("unFade");
    name.classList.remove("fade");
    name.hidden = false;
    
    status.classList.add("unFade");
    status.classList.remove("fade");
    status.hidden = false;

    statusDetails.hidden = false;

    details.style = "background-image: url(static/images/avatar_bg.png)";

    return true;
}