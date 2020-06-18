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
    <div class="avatar-container align-right" >
        <div class="avatar-image" >
            <img id="avatar" class="avatar" src="<%= request.getContextPath() %>/static/images/person_no_avatar.png" alt="img">
        </div>
        <div class="avatar-username" >
            <p><%= session.getAttribute("username") == null ? "img" : session.getAttribute("username") %></p>
        </div>
    </div>
</div>