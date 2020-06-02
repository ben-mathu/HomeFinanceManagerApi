document.addEventListener("onload", function (event) {
    sendCode();
});

function sendCode() {
    document.getElementById("progress").hidden = false;
    document.getElementById("message").innerHTML = "Sending Code";
    document.getElementById("code").disabled = true;
    var request = new XMLHttpRequest();

    try {
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                if (request.status == 200) {
                    document.getElementById("progress").hidden = true;
                    document.getElementById("code").disabled = false;
                }
            }
        }

        var email = "email=" + escape(document.getElementById("email").value);
        var data = email;

        request.open("POST", "registration/get-confirmation-code", true);
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        request.send(data)
    } catch (e) {
        console.log("Error caused by: " + e);
    }
}