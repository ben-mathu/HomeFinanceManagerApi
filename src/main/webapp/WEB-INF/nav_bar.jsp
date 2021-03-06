<div id="navigationDrawer" class="nav-container">
    <div id="navIcon" class="nav-icon-container" >
        <img id="navBarIcon" class="nav-icon" src="${pageContext.request.contextPath}/static/images/nav_menu_bar.png" alt="nav-icon" onclick="openOrCloseNav()">
    </div>
<!--    <div id="details" class="user-details-container" >
        <div class="nav-avatar-container" >
            <span id="statusIndicatorAvatar" class="status-indicator"></span>
            <img id="avatar" class="nav-avatar avatar" src="${pageContext.request.contextPath}/static/images/person_no_avatar.png" alt="img">
        </div>
        <div class="status-section">
            <div class="user-name">
                <span class="name" id="userName" style="color: #3F3A4B;" hidden>Matt LeBlanc</span>
            </div>
            <div class="status">
                <span id="statusIndicator" class="status-indicator" hidden></span>
                <span class="status-text" id="status" style="color: #3F3A4B;" hidden>Online</span>
            </div>
        </div>
    </div>-->
    <div class="horizontal-separator"></div>
    <div class="menu-container" >
        <div id="menu" class="menu">
            <div id="home" class="menu-item">
                <img class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/home_2.png" alt="image_1">
                <div class="menu-title" >
                    <input type="hidden" id="home-title" name="home" value="mainContent">
                    Dashboard
                </div>
            </div>
<!--            <div id="messages" class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/messages.png" alt="image_1">
                <div class="menu-title" >
                    <input type="hidden" id="messages-title" name="settings" value="Messages">
                    Messages
                </div>
            </div>-->
<!--            <div id="members" class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/members.png" alt="image_1">
                <div class="menu-title" >
                    <input type="hidden" id="members-title" name="settings" value="Members">
                    Members
                </div>
            </div>-->
            <div id="settings" class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/settings.png" alt="image_1">
                <div class="menu-title" >
                    <input type="hidden" id="settings-title" name="settings" value="Settings">
                    Settings
                </div>
            </div>
            <div id="guide" class="menu-item">
                <img  class="menu-item-icon" src="${pageContext.request.contextPath}/static/images/guide.png" alt="image_1">
                <div class="menu-title" >
                    <input type="hidden" id="guide-title" name="guide" value="Guide">
                    Guide
                </div>
            </div>
        </div>
    </div>
</div>