<% 
	String pageTitle = "Confirm Registration";
    String scripts = "static/js/ajax.js";

	pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("script", scripts);
%>
<%@ include file = "../page_setting_top.jsp" %>
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
        <form class="confirm-code-container" method="POST">
            <input type="hidden" id="username" name="username" value="<%= session.getAttribute("username")%>">
            <input type="hidden" id="password" name="password" value="<%= session.getAttribute("password")%>">
            <div class="input-container">
                <div>
                    <input class="input-style" id="email" type="hidden" name="email" placeholder="Email: example@gmail.com" />
                    <span id="email-error"></span>
                </div>
            </div>
            
            <div class="input-container">
                <div>
                    <input class="input-style" id="code" type="text" name="code" placeholder="999-999" />
                    <span id="code-error" disabled></span>
                </div>
            </div>

            <div class="code-sender">
                <input class="link confirm-email" type="submit" value="Send the code again" onclick="sendCode()" />
            </div>
            
            <div class="btn-code">
                <button class="btn2 submit" onclick="sendRequest()">Submit</button>
            </div>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/static/js/confirm_code.js"></script>
<%@ include file = "../page_setting_bottom.jsp" %>