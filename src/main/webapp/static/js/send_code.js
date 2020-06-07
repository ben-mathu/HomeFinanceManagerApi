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

function sendCode(isCodeSent){
    if (isCodeSent) {
        document.getElementById("message").innerHTML = "Sending Code...";

        var request = getXmlHttpRequest();

        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    console.log("Code has been sent...")
                }
            }
        }

        var email = "email=" + escape(document.getElementById("email").value);
        var data = email;

        sessionStorage.setItem("isAlreadySent", true)
        request.open("POST", "confirm-user/get-confirmation-code", true);
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        request.send(data);
    }
}

setTimeout(() => {
    if (!sessionStorage.getItem("isAlreadySent")) {
        sendCode(true);
    }
}, 500);