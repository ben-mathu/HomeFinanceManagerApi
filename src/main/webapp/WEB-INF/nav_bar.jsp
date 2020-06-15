<div id="navigationDrawer" class="nav-container" >
    <div id="navIcon" class="nav-icon-container" >
        <img id="navBarIcon" class="nav-icon" src="${pageContext.request.contextPath}/static/images/nav_menu_bar.png" alt="nav-icon" onclick="openOrCloseNav()">
    </div>
    <div id="details" class="user-details-container" >
        <div class="avatar-container nav-avatar-container" >
            <span id="statusIndicatorAvatar" class="status-indicator"></span>
            <img id="avatar" class="nav-avatar avatar" src="${pageContext.request.contextPath}/static/images/person_no_avatar.png" alt="img">
        </div>
        <div class="status-section" >
            <div class="user-name" >
                <span class="name" id="userName" hidden>Matt LeBlanc</span>
            </div>
            <div class="status">
                <span id="statusIndicator" class="status-indicator" hidden></span>
                <span class="status-text" id="status" hidden>Online</span>
            </div>
        </div>
    </div>
    <div class="horizontal-separator"></div>
    <div class="menu-container" >
        <div class="menu">
            <div class="menu-item">
                <img class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/dashboard.png" alt="image_1">
                <div class="menu-title" >
                    Dashboard
                </div>
            </div>
            <div class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/messages.png" alt="image_1">
                <div class="menu-title" >
                    Messages
                </div>
            </div>
            <div class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/members.png" alt="image_1">
                <div class="menu-title" >
                    Members
                </div>
            </div>
            <div id="settings" class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/settings.png" alt="image_1">
                <div class="menu-title" >
                    Settings
                </div>
            </div>
        </div>
    </div>
</div>