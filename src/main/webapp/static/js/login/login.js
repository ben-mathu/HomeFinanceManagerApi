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

function loginUser() {
    document.getElementById("progress").hidden = false;
    document.getElementById("usernameError").innerHTML = "";
    document.getElementById("passwordError").innerHTML = "";
    document.getElementById("result").innerHTML = "";
    document.getElementById("result").hidden = true;

    var request = getXmlHttpRequest();
    try {
        request.onreadystatechange = function() {
            if (request.readyState == 4) {
                if (request.status == 400) {

                    var obj = JSON.parse(request.responseText);
                    document.getElementById("usernameError").innerHTML = obj.username_error;
                    document.getElementById("passwordError").innerHTML = obj.password_error;
                    document.getElementById("progress").hidden = true;
                } else if (request.status == 403) {

                    var error = JSON.parse(request.responseText);
                    document.getElementById("result").innerHTML = error.message;
                    document.getElementById("progress").hidden = true;
                    document.getElementById("result").hidden = false;
                } else if (request.status == 200) {

                    var path = document.getElementById("contextPath").value;

                    obj = JSON.parse(request.responseText);
                    window.localStorage.setItem("token", obj.report.token);
                    window.localStorage.setItem("user_id", obj.user.user_id);
                    window.localStorage.setItem("username", obj.user.username);

                    document.getElementById("result").innerHTML = "<span style=\"color: green;\">Success. Please wait while you are redirected...</span>";
                    document.getElementById("progress").hidden = true;
                    window.location.href = path + "/dashboard";
                } else {
                    document.getElementById("result").hidden = true;
                    document.getElementById("result").innerHTML = "Error: Contact Developer: hfms.mathu@gmail.com";
                }
            }
        }

        // if ( !! window.FormData) {
        //     var formData = new FormData();
        //     formData.append("file", form);
        //     request.send(formData);
        // }

        var username = "username=" + escape(document.getElementById("username").value);
        var password = "password=" + escape(document.getElementById("password").value);
        var data = username + "&" + password;

        request.open("POST", "login-user", true);
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        request.send(data);
    } catch(e) {
        console("Unable to connect to server");
    }
}