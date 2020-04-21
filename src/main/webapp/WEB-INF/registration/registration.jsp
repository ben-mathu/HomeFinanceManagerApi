<%
	boolean validParams = false;
	String pageTitle = "Registration";
	String scripts = "static/js/ajax.js";
	pageContext.setAttribute("title", pageTitle);
	pageContext.setAttribute("script", scripts);
	pageContext.setAttribute("registration", "static/js/registration.js");
%>
<%@ include file = "../page_setting_top.jsp" %>
    <div class="container text-center">
        <div class="row">
            <div class="sections first-half">
                <a href="<%= request.getContextPath() %>"><img src="${pageContext.request.contextPath}/static/images/logo.png" alt="HFMS Logo" height="150px" width="150px" /></a>
                <div>
                    <p>Manage your Home na jamaa yako.</p>
                </div>
            </div>
            
            <div class="sections vertical-line"></div>
            <div class="sections second-half">
            	<div>
                    <span id="result" class="resp">${error.usernameError}</span>
            	</div>
                <div class="form-control">
                    <form class="form-container" method="POST" onsubmit="return false;">
                        <input type="hidden" id="contextPath" name="path" value="${pageContext.request.contextPath}">

                        <%-- Enter email --%>
                    	<div class="input-container">
                            <div>
                                <span id="emailError" class="resp" hidden>${emailError.emailError}</span>
                            	<input class="input-style span-block" id="email" type="text" name="email" placeholder="Email" value="${email}">
                            </div>
                        </div>

                        <%-- User's username --%>
                        <div class="input-container">
                            <div>
                                <span id="usernameError" class="resp" hidden>${usernameError.usernameError}</span>
                            	<input class="input-style span-block" id="username" type="text" name="username" placeholder="Username" value="${username}">
                            </div>
                        </div>

                        <%-- User password --%>
                        <div class="input-container">
                            <div>
                                <span id="passwordError" class="resp" hidden>${passwordError.passwordError}</span>
                                <div class="span-block">
                                    <input class="input-style" id="password" type="password" name="password" placeholder="Password">
                                    <span for="password" style="height: fit-content; margin-top: 20px;" hidden>show</span>
                                </div>
                            </div>
                        </div>

                        <%-- join household checkbox --%>
                        <div class="checkbox-container">
                            <div id="checkboxItem" class="checkbox" >
                                Join a Household?
                                <input class="checkbox-style" id="checkbox" type="checkbox">
                            </div>
                        </div>
                        
                        <%-- Household name --%>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="householdName" type="text" name="household" placeholder="Household Name (Optional)">
                            </div>
                            <span id="householdNameError" class="resp" hidden>${householdNameError.householdNameError}</span>
                        </div>
                        
                        <%-- Household Unique id field --%>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="householdId" type="text" placeholder="Household Unique Id (Optional)" hidden>
                            </div>
                            <span id="householdIdError" class="resp" hidden>${householdIdError.householdIdError}</span>
                        </div>
                        <div class="submit-area">
                            <button class="btn2 submit" onclick="registerUser()" id="submit">Submit</button>
                        </div>
                    </form>
                    <a class="link register-link" href="login">Already have an account? Login.</a>
                </div>
                <%-- <div class="login-using-socials">
                    <a href="https://facebook.com"><div class="image-size">
                        <img src="${pageContext.request.contextPath}/static/images/facebook.png" alt="Facebook">
                    </div></a>

                    <a href="https://twitter.com"><div class="image-size">
                        <img src="${pageContext.request.contextPath}/static/images/twitter.png" alt="Twitter">
                    </div></a>

                    <a href="https://google.com"><div class="image-size">
                        <img src="${pageContext.request.contextPath}/static/images/google.png" alt="Google">
                    </div></a>
                </div> --%>
            </div>
        </div>
    </div>
<%@ include file = "../page_setting_bottom.jsp" %>
