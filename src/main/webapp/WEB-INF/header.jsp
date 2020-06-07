<div class="header-container" >
    <div class="avatar-container align-right" >
        <div class="avatar-image" >
            <img id="avatar" class="avatar" src="${pageContext.request.contextPath}/static/images/person_no_avatar.png" alt="img">
        </div>
        <div class="avatar-username" >
            <p><%= session.getAttribute("username") == null ? "img" : session.getAttribute("username") %></p>
        </div>
    </div>
    <div class="time-container" >
        <h6><%= (new SimpleDateFormatter("HH:mm").format(new Date())).toString() %></h6>
    </div>
</div>