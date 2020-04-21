<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
    	<link rel="shortcut icon" type="image/ico" href="static/images/favicon.ico" />
        <link rel="stylesheet" type="text/css" href="static/css/style.css?time=<%= (new java.util.Date()).toLocaleString()%>"/>

        <title>HFMS - Welcome</title>

        <script>
            function checkToken() {
                var token = window.localStorage.getItem("token");
                var data = "token=" + escape(token);

                if (token !== undefined) {
                    if (token.length > 0) {
                        document.getElementById("nav-buttons").hidden = true;
                        document.getElementById("nav-dashboard").hidden = false;
                    }
                }
            }

            setTimeout(() => {
                checkToken();
            }, 500);
        </script>
    </head>
    <body>
        <div class="container items-center text-center">
            <div>
                <img src="static/images/logo.png" alt="HFMS logo" height="150px" width="150px" />
                <div>
                    <p>Manage your Home na jamaa yako.</p>
                </div>
            </div>

            <div id="nav-buttons">
                <div class="btn-containers">
                    <a class="btn2 text-center" href="login">Login</a>
                </div>
                <div class="btn-containers">
                    <a class="btn2 text-center" href="registration">Register</a>
                </div>
            </div>

            <div id="nav-dashboard" hidden>
                <div class="btn-containers">
                    <a class="btn2 text-center" href="dashboard">To Dashboard</a>
                </div>
            </div>
        </div>

        <p class="footer text-muted text-center">
            <code>Twitter</code><a class="link" href="https://twitter.com/beniemathu">@beniemathu </a>
            <code>GitHub</code><a class="link" href="https://github.com/ben-mathu">@ben-mathu </a>
            2017-2019
        </p>
    </body>
</html>