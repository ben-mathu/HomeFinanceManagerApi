<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
    	<link rel="shortcut icon" type="image/ico" href="${pageContext.request.contextPath}/static/images/favicon.ico" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/style.css?<%= (new java.util.Date()).toLocaleString()%>" />
    
        <title>${title}</title>

        <script src="${pageContext.request.contextPath}/${script}"></script>
    </head>
    <body>