<div class="header-container" >
    <div class="nav-icon-container" >
        <img class="nav-icon" src="<%= request.getContextPath() %>/static/images/logo.png" alt="nav-icon">
    </div>
    <div class="breadcrumb-container" >
        <span id="breadcrumb" class="breadcrumb"></span>
    </div>
    <div class="time-container text-center" >
        <h4 id="timeNow"></h4>
    </div>
    <div class="align-right" >
        <div id="avatarContainer" class="avatar-container" >
            <div class="avatar-image" >
                <img id="avatar" class="avatar" src="<%= request.getContextPath() %>/static/images/person_no_avatar.png" alt="img">
            </div>
            <div class="avatar-username" >
                <span><%= session.getAttribute("username") == null ? "img" : session.getAttribute("username") %></span>
            </div>
        </div>
        <div id="optionsMenu" class="menu-options">
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