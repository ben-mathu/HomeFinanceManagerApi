<%
	boolean validParams = false;
	String pageTitle = "Registration";
	String scripts = "static/js/ajax.js";
	pageContext.setAttribute("title", pageTitle);
	pageContext.setAttribute("script", scripts);
%>
<%@ include file = "../page_setting_top.jsp" %>
    <div class="container text-center">
        <div class="row">
            <div class="sections first-half">
                <img src="${pageContext.request.contextPath}/static/images/logo.png" alt="HFMS Logo" height="150px" width="150px" />
                <div>
                    <p>Manage your Home na jamaa yako.</p>
                </div>
            </div>
            
            <div class="sections vertical-line"></div>
            <div class="sections second-half">
            	<div>
            		<span id="result" class="resp">${error.usernameError}</span>
            	</div>
                <div>
                    <form class="form-container" method="POST">
                    	<div class="input-container">
                            <div>
                            	<input class="input-style" id="email" type="text" name="email" placeholder="Email" value="${email}">
                            </div>
                            <span id="emailError" class="resp">${emailError.emailError}</span>
                        </div>
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
                    </form>
                    <div class="submit-area">
                            <button class="btn2 submit" onclick="registerUser()" id="submit">Submit</button>
                    </div>
                    <a class="link register-link" href="login">Already have an account?</a>
                </div>
                <div class="login-using-socials">
                    <a href="https://facebook.com"><div class="image-size">
                        <img src="${pageContext.request.contextPath}/static/images/facebook.png" alt="Facebook">
                    </div></a>

                    <a href="https://twitter.com"><div class="image-size">
                        <img src="${pageContext.request.contextPath}/static/images/twitter.png" alt="Twitter">
                    </div></a>

                    <a href="https://google.com"><div class="image-size">
                        <img src="${pageContext.request.contextPath}/static/images/google.png" alt="Google">
                    </div></a>
                </div>
            </div>
        </div>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>
