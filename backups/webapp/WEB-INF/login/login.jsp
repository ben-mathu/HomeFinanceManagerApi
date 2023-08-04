<% 
	String pageTitle = "Login";
	pageContext.setAttribute("title", pageTitle);
    pageContext.setAttribute("login", "static/js/login/login.js");
%>
<%@ include file="../page_setting_top.jsp" %>
    <div class="container text-center">
        <div class="row text-center">
            <div class="sections first-half">
                <a href="<%= request.getContextPath() %>"><img src="static/images/logo.png" alt="HFMS Logo" height="150px" width="150px" /></a>
                <div class="tagline">
                    <p>Manage your finances na jamaa yako.</p>
                </div>
            </div>
            
            <div class="sections vertical-line"></div>

            <div class="sections second-half">
                <div>
            		<span id="result" class="resp">${error.usernameError}</span>
            	</div>
                <div>
                    <form method="POST" onsubmit="return false;">
                        <input type="hidden" id="contextPath" name="contextPath" value="${pageContext.request.contextPath}">
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="username" type="text" name="username" placeholder="Username" value="${username}">
                            </div>
                            <span id="usernameError" class="resp">${usernameError.usernameError}</span>
                        </div>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="password" type="password" name="password" placeholder="Password">
                            </div>
                            <span id="passwordError" class="resp">${passwordError.passwordError}</span>
                        </div>
                        <div class="submit-area">
                            <button class="btn2 submit" onclick="loginUser()" id="submit">Login</button>
                        </div>
                    </form>
                    <a class="link register-link" href="registration">Create an account.</a>
                </div>
                <%-- <div class="login-using-socials">
                    <div class="image-size">
                        <img src="static/images/facebook.png" alt="Facebook">
                    </div>

                    <div class="image-size">
                        <img src="static/images/twitter.png" alt="Twitter">
                    </div>

                    <div class="image-size">
                        <img src="static/images/google.png" alt="Google">
                    </div>
                </div> --%>
            </div>
        </div>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>
