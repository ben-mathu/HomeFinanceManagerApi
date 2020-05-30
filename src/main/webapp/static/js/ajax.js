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

function registerUser() {
    console.log("I have reached the highest peak...of...incopetence.");

    var request = getXmlHttpRequest();
    try {
        request.onreadystatechange = function() {
            if (request.readyState == 4) {
                if (request.status == 400) {
                    var obj = JSON.parse(request.responseText);
                    document.getElementById("emailError").innerHTML = obj.email_error;
                    
                    document.getElementById("usernameError").innerHTML = obj.username_error;
                    
                    document.getElementById("passwordError").innerHTML = obj.password_error;
                } else if (request.status == 403) {
                    var error = JSON.parse(request.responseText);
                    document.getElementById("result").innerHTML = error.message;
                } else {
                    document.getElementById("result").innerHTML = "<span style=\"color: green;\">Success, wait while you are redirected...</span>"
                }
            }
        }

        // if ( !! window.FormData) {
        //     var formData = new FormData();
        //     formData.append("file", form);
        //     request.send(formData);
        // }

        var email = "email=" + escape(document.getElementById("email").value);
        var username = "username=" + escape(document.getElementById("username").value);
        var password = "password=" + escape(document.getElementById("password").value);
        var data = email + "&" + username + "&" + password;

        request.open("POST", "registration/register-user", true);
        request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        request.send(data);
    } catch(e) {
        console("Unable to connect to server");
    }
}