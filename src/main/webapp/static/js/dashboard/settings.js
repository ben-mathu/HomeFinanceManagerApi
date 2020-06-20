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

function configureSettings() {
    document.getElementById("logout").onclick = function() {
        logout();
    }
}

function logout() {
    window.localStorage.setItem("token", "");
    window.localStorage.setItem("user_id", "");
    window.localStorage.setItem("username", "");

    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    var contextPath = document.getElementById("contextPath").value;

    window.location.href = contextPath + "/login";
}