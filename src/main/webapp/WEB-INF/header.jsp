<div class="header-container" >
    <div class="header-icon-container align-inline" >
        <a href="<%= request.getContextPath() %>"><img class="header-icon" src="<%= request.getContextPath() %>/static/images/logo.png" alt="nav-icon"></a>
    </div>
    <div class="breadcrumb-container align-inline" >
        <span id="breadcrumb" class="breadcrumb"></span>
    </div>
    <%-- show time now --%>
    <div class="time-container align-inline" >
        <span class="time-now" id="timeNow"></span>
    </div>
    <div class="user-section align-inline">
        <div id="avatarContainer" class="avatar-container align-inline" >
            <div class="avatar-image align-inline" >
                <img id="avatar" class="avatar" src="<%= request.getContextPath() %>/static/images/person_no_avatar.png" alt="img">
            </div>
            <div class="avatar-username align-inline" >
                <span class="username-element" id="username"><%= session.getAttribute("username") == null ? "Your username" : session.getAttribute("username") %></span>
            </div>
        </div>

        <%-- notification area --%>
<!--        <div class="notification-container align-inline" >
            <div class="notification align-inline" >
                <img class="notification-item-icon" src="<%= request.getContextPath() %>/static/images/ic_notification.png" alt="Notifications">
            </div>
        </div>-->

        <div id="optionsMenu" class="menu-options">
            <div class="income-menu" >
                <span id="income" class="income" hidden>Income: </span>
                <input id="openIncomeModal" type="button" value="Add Income">
<!--                <span id="walletAmount" class="wallet" hidden>Wallet:</span>
                <input id="btnAddWallet" type="button" name="top-up" value="Top Up Wallet">-->
            </div>
            <div class="horizontal-separator"></div>
            <div id="settings-item" class="option-menu-item" >
                <input type="hidden" id="settings-option" name="settings" value="Settings">
                Settings
            </div>
            <div id="option-logout" class="option-menu-item" >
                Logout
            </div>
        </div>
    </div>
</div>