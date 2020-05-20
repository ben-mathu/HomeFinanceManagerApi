<%@ include file = "page_setting_top.jsp" %>
    <div class="container text-center">
        <div class="row">
            <div class="sections first-half">
                <img src="static/images/logo.png" alt="HFMS Logo" height="150px" width="150px" />
                <div>
                    <p>Manage your finances na jamaa yako.</p>
                </div>
            </div>
            
            <div class="sections vertical-line"></div>

            <div class="sections second-half">
                <div>
                    <form action="api/login" method="POST">
                        <input class="input-style" id="email" type="input" name="email" placeholder="Email"><br>
                        <input class="input-style" id="username" type="input" name="username" placeholder="Username"><br>
                        <input class="input-style" id="password" type="input" name="password" placeholder="Password"><br>
                        <div class="submit-area">
                            <input class="btn2 submit" type="submit" name="login" value="Login">
                        </div>
                        <a class="link register-link" href="login">Already have an account?</a>
                    </form>
                </div>
                <div class="login-using-socials">
                    <div class="image-size">
                        <img src="static/images/facebook.png" alt="Facebook">
                    </div>

                    <div class="image-size">
                        <img src="static/images/twitter.png" alt="Twitter">
                    </div>

                    <div class="image-size">
                        <img src="static/images/google.png" alt="Google">
                    </div>
                </div>
            </>
        </div>
    </div>
<%@ include file = "page_setting_bottom.jsp" %>