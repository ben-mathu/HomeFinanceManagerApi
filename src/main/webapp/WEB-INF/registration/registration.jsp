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
                        <input type="hidden" id="contextPath" name="path" value="${pageContext.request.contextPath}">

                        <%-- Enter email --%>
                    	<div class="input-container">
                            <div>
                            	<input class="input-style" id="email" type="text" name="email" placeholder="Email" value="${email}">
                            </div>
                            <span id="emailError" class="resp">${emailError.emailError}</span>
                        </div>

                        <%-- User's username --%>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="username" type="text" name="username" placeholder="Username" value="${username}">
                            </div>
                            <span id="usernameError" class="resp">${usernameError.usernameError}</span>
                        </div>

                        <%-- User password --%>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="password" type="password" name="password" placeholder="Password">
                            </div>
                            <span id="passwordError" class="resp">${passwordError.passwordError}</span>
                        </div>

                        <%-- join household checkbox --%>
                        <div class="checkbox-container">
                            <div id="checkboxItem" class="checkbox" >
                                Join a Household?
                                <input class="checkbox-style" id="checkbox" type="checkbox">
                            </div>
                        </div>
                        
                        <%-- Household name --%>
                        <span id="householdFields" style="margin-top: 3px; font-size: 13px; color: #FEC800">You would be able to create a house once you have registered. It is okay to leave this fields blank</span>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="householdName" type="text" name="household" placeholder="Household Name">
                            </div>
                            <span id="householdNameError" class="resp">${householdNameError.householdNameError}</span>
                        </div>

                        <%-- Household Description --%>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="householdDesc" type="text" name="householdDesc" placeholder="Household Description">
                            </div>
                            <span id="householdDescError" class="resp">${householdDescError.householdDescError}</span>
                        </div>
                        
                        <%-- Household Unique id field --%>
                        <div class="input-container">
                            <div>
                            	<input class="input-style" id="householdId" type="text" placeholder="Household Unique Id" hidden>
                            </div>
                            <span id="householdIdError" class="resp">${householdIdError.householdIdError}</span>
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
