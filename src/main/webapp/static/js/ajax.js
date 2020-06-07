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


function changeEmail() {
    if(document.getElementById("email").type == 'hidden') {
        document.getElementById("email").type = "text";
        document.getElementById("code").type = "hidden";

        document.getElementById("code-error").innerHTML = "";
        document.getElementById("email-error").innerHTML = "";
    }
}

function sendRequest() {
    document.getElementById("progress").hidden = false;
    var request = getXmlHttpRequest();
    
    try {
        document.getElementById("code-error").innerHTML = "";
        document.getElementById("email-error").innerHTML = "";
        if (document.getElementById("email").hidden) {
            var path = document.getElementById("contextPath").value;

            // to check if code submitted is valid
            request.onreadystatechange = function() {
                if (request.readyState == 4) {
                    if (request.status == 404) {
                        console.log("no page");
                    }
                    if (request.status == 406) {
                        var obj = JSON.parse(request.responseText);
                        document.getElementById("code-error").innerHTML = obj.message;
                        document.getElementById("code-sender").hidden = false;
                        document.getElementById("progress").hidden = true;
                    } else {
                        document.getElementById("code-error").innerHTML = "Please stand by...";
                        document.getElementById("progress").hidden = true;
                        window.location.href = path + "/dashboard";
                    }
                }
            }

            var email = "email=" + escape(document.getElementById("email").value);
            var username = "username=" + escape(document.getElementById("username").value);
            var password = "password=" + escape(document.getElementById("password").value);
            var code = "code=" + escape(document.getElementById("code").value);
            var data = email + "&" + username + "&" + password + "&" + code;

            request.open("POST", path + "/registration/confirm-user/email-confirmation", true);
            request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            request.send(data);
        } else {

            // checks whether email submited is valid
            request.onreadystatechange = function() {
                if (request.readyState == 4) {
                    if (request.status != 200) {
                        document.getElementById("email-error").innerHTML = request.responseText;
                        document.getElementById("progress").hidden = true;
                    } else {
                        document.getElementById("email-error").innerHTML = "Please stand by...";
                        document.getElementById("progress").hidden = true;
                        window.location.href = request.responseText;
                    }
                }
            }

            email = "email=" + escape(document.getElementById("email").value);
            username = "username=" + escape(document.getElementById("username").value);
            password = "password=" + escape(document.getElementById("password").value);
            data = email + "&" + username + "&" + password;
            
            var contextPath = document.getElementById("contextPath").value;
            request.open("POST", contextPath + "/change-email-address", true);
            request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            request.send(data);
        }

    } catch(e) {
        console.log("Error caused by: " + e);
    }
}

function registerUser() {
    document.getElementById("progress").hidden = false;
    document.getElementById("emailError").innerHTML = "";
    document.getElementById("usernameError").innerHTML = "";
    document.getElementById("passwordError").innerHTML = "";
    document.getElementById("result").innerHTML = "";

    var request = getXmlHttpRequest();
    try {
        request.onreadystatechange = function() {
            if (request.readyState == 4) {
                if (request.status == 400) {
                    var obj = JSON.parse(request.responseText);
                    document.getElementById("emailError").innerHTML = obj.email_error;
                    
                    document.getElementById("usernameError").innerHTML = obj.username_error;
                    
                    document.getElementById("passwordError").innerHTML = obj.password_error;

                    document.getElementById("progress").hidden = true;
                } else if (request.status == 403) {
                    var error = JSON.parse(request.responseText);
                    document.getElementById("result").innerHTML = error.message;
                    document.getElementById("progress").hidden = true;
                } else {
                    document.getElementById("result").innerHTML = "<span style=\"color: green;\">Success. Please wait while you are redirected...</span>";
                    window.location.href = request.responseText;
                    document.getElementById("progress").hidden = true;
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