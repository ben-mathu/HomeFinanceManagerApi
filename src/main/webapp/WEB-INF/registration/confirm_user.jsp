<% 
	String pageTitle = "Confirm Registration";
    String scripts = "static/js/ajax.js";

	pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("script", scripts);
    pageContext.setAttribute("coder", "static/js/send_code.js");
    pageContext.setAttribute("timer", "static/js/dashboard/dashboard.js");
    boolean isCodeSent = false;
%>
<%@ include file = "../page_setting_top.jsp" %>
    <%@ include file = "../header.jsp" %>
    <div class="container">
        <div class="info" >
            <p class="text-center">Use the code sent to your email for confirmation.</p>
            <div class="links" >
                <p>
                    <%= session.getAttribute("email") == null ? "" : session.getAttribute("email") %>
                    <input class="link confirm-email" type="submit" value="Change Email" onclick="changeEmail()" />
                </p>
            </div>
        </div>
        <form id="form" class="confirm-code-container" method="POST">
            <input type="hidden" id="username" name="username" value="<%= session.getAttribute("username")%>">
            <input type="hidden" id="password" name="password" value="<%= session.getAttribute("password")%>">
            <input type="hidden" id="contextPath" name="path" value="${pageContext.request.contextPath}">
            <input type="hidden" id="token" name="token" value="<%= session.getAttribute("token") %>">
            <div class="input-container">
                <div>
                    <input class="input-style" id="email" type="text" name="email" placeholder="Email: example@domainname.com"  value="<%= session.getAttribute("email") %>" hidden/>
                    <span id="email-error"></span>
                </div>
            </div>
            
            <div class="input-container">
                <div>
                    <input class="input-style" id="code" type="text" name="code" placeholder="999-999" />
                    <span id="code-error"></span>
                </div>
            </div>
            
            <div id="code-sender" class="code-sender text-center" hidden>
                <input class="link confirm-email" type="submit" value="Send the code again" onclick="sendCode(true)" />
            </div>

            <div class="btn-code">
                <button class="btn2 submit" type="button" onclick="sendRequest()">Submit</button>
            </div>
        </form>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>