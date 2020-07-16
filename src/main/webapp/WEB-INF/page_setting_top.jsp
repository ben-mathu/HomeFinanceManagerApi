<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
    	<link rel="shortcut icon" type="image/ico" href="<%= request.getContextPath() %>/static/images/favicon.ico" />
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/static/css/style.css?<%= (new java.util.Date()).toLocaleString()%>" />
    
        <title>${title}</title>

        <script src="<%= request.getContextPath() %>/${script}"></script>
        <script src="<%= request.getContextPath() %>/${registration}"></script>
        <script src="<%= request.getContextPath() %>/${coder}"></script>
        <script src="<%= request.getContextPath() %>/${timer}"></script>
        <script src="<%= request.getContextPath() %>/${login}"></script>
        <script src="<%= request.getContextPath() %>/${dashboard}"></script>
        <script src="<%= request.getContextPath() %>/${settings}"></script>
        <script src="<%= request.getContextPath() %>/${schedule}"></script>
        <script src="<%= request.getContextPath() %>/${income}"></script>
        <script src="<%= request.getContextPath() %>/${grocery}"></script>
        <script src="<%= request.getContextPath() %>/${household}"></script>
        <script src="<%= request.getContextPath() %>/${envelopes}"></script>
        <script src="<%= request.getContextPath() %>/${expenses}"></script>
    </head>
    <body onLoad="JavaScript:loadWindow();">