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
                if (request.status != 200) {
                    var obj = JSON.parse(request.responseText);
                    document.getElementById("code-error").innerHTML = obj.message;
                    document.getElementById("code-sender").hidden = false;
                    document.getElementById("progress").hidden = true;
                } else {
                    console.log("Nothing to do, code has been sent.");
                    document.getElementById("progress").hidden = true;
                    document.getElementById("code-sender").hidden = true;
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
        document.getElementById("progress").hidden = false;
        sendCode(true);
    }
}, 500);