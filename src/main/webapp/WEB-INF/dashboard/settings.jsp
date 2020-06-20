<div id="Settings" class="settings-container" hidden>
    <input type="hidden" id="contextPath" name="context_path" value="<%= request.getContextPath() %>">
    <div class="settings-section" >
        <div class="settings-menu setting-element" >
            <div class="settings-menu-options" >
                <div class="account-option setting-option-style selected-option" >
                    <span>Account</span>
                </div>
                <div class="preferences-option setting-option-style" >
                    <span>Preferences</span>
                </div>
            </div>
            <div class="save-settings" >
                <input class="btn2 save-button" type="button" name="submit" value="Save">
            </div>
        </div>
        <div class="settings-details setting-element" >
            <%@ include file = "00_account.jsp" %>
            <%@ include file = "01_preferences.jsp" %>
        </div>
    </div>
</div>