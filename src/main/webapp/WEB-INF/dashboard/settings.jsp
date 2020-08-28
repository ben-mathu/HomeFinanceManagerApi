<div id="Settings" class="settings-container" hidden>
    <input type="hidden" id="contextPath" name="context_path" value="<%= request.getContextPath() %>">
    <div class="settings-section" >
        <div class="settings-menu setting-element" >
            <div class="settings-menu-options" >
                <div id="accountSettings" class="setting-option-style selected-option" >
                    <span>Account</span>
                </div>
                
                <div id="householdSettings" class="setting-option-style" >
                    <span>Household</span>
                </div>
                
                <div id="preferencesSettings" class="setting-option-style" >
                    <span>Preferences</span>
                </div>
                
                <div class="save-settings" >
                    <input id="btnSaveAccountDetails" class="btn2 save-button" type="button" name="submit" value="Save">
                </div>
            </div>
        </div>
        <div class="settings-details setting-element" >
            <%@ include file = "00_account.jsp" %>
            <%@ include file = "01_preferences.jsp" %>
            <%@ include file = "02_household.jsp" %>
        </div>
    </div>
</div>