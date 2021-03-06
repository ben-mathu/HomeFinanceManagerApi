<div id="accountContainer" class="account-container" >
    <input type="hidden" id="contextPath" name="context_path" value="<${pageContext.request.contextPath}">
    <div class="change-password" >
        <span for="passwordChange"></span>
        <form id="passwordChange">
            <div>
                <input id="newPassword" class="input-style" type="text" name="password" placeholder="Password" value=""/>
                <span for="newPassword" style="height: fit-content; margin-top: 20px;" hidden>show</span>
            </div>
            <div>
                <input id="confirmPassword" class="input-style" type="password" name="confirm-password" placeholder="Confirm Password" value=""/>
                <span for="confirmPassword" style="height: fit-content; margin-top: 20px;" hidden>show</span>
            </div>
            <div>
                <input id="btnChangePassword" class="btn2" type="button" name="submit" value="Submit"/>
            </div>
        </form>
    </div>
    <div class="email-segment" >
        <input id="inputEmail" class="input-style" type="text" name="email" placeholder="<%= session.getAttribute("email") %>"/>
    </div>
    <div class="username-segment" >
        <input id="inputUsername" class="input-style" type="text" name="email" placeholder="<%= session.getAttribute("username") %>"/>
    </div>
    <div class="number-segment" >
        <input id="inputMobNumber" class="input-style" type="hidden" name="mob-number" placeholder="<%= session.getAttribute("mob_number") %>"/>
        <input id="btnAddMobNum" class="btn2" type="button" name="button" value="+ Add Number"/>
    </div>
    <div class="logout-segment" >
        <input id="logout" class="btn4-caution" type="button" name="logout" value="Logout">
    </div>
    <div class="delete-segment" >
        <span for="btnDeleteAccount"></span>
        <input id="btnDeleteAccount" class="btn3-warn" type="button" name="delete" value="Delete Account">
        <p class="error-text">If you choose to delete your account, please consider</br> that you will not be able to recover it.</p>
    </div>
</div>