function configureMembers() {

}

function setMembers() {
    let template = document.getElementById("myMemberTemplate");

    let memberList = document.getElementById("memberList");
    
    householdMembers.forEach(member => {

        template.content.querySelector("span").textContent = member.username;
        let templateClone = document.importNode(template.content, true);

        memberList.appendChild(templateClone);
    });
}