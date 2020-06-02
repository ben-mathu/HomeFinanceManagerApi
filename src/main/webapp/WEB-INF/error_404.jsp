<%@ page contentType="text/html;charset=utf-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Error - Lost and found?</title>
        
        <meta content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css?<%= (new java.util.Date()).toLocaleString()%>" />
    </head>
    <body>
        <div class="container text-center">
			<h1>Are you lost?</h1>
			<form action="${pageContext.request.contextPath}" method="get">
				Use this button to navigate back <input class="btn2" type="submit" value="Home">
			</form>
        </div>
    </body>
</html>