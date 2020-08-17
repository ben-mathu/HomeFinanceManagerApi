// household settings elements
let householdInviteCode;

let household = {
    household_id: "",
    household_name: "",
    description: ""
}

function configureHousehold() {
    householdInviteCode = document.getElementById("householdInviteCode");
}

function setHouseholdId() {
    let householdContainer = document.getElementById("householdContainer");
    let count = 0;
    householdArr.forEach(household => {
        let input = document.createElement("input");
        input.type = "text";
        input.disabled = true;
        input.id = household.household_id;
        input.value = household.household_id;
        input.className = "input-style";
        
        let label = document.createElement("label");
        label.htmlFor = household.household_id;
        label.innerHTML = "House of " + household.household_name + ": ";

        let isOwner = userHouseholdArr[count].is_owner;
        let textOwnership = "";
        if (isOwner) {
            textOwnership = "You are the Owner";
        } else {
            textOwnership = "You are a Member";
        }
        let ownership = document.createElement("span");
        ownership.style = "color: #FEC800;"
        ownership.innerHTML = textOwnership;

        let div = document.createElement("div");
        div.appendChild(label);
        div.appendChild(input);
        div.appendChild(ownership);

        householdContainer.appendChild(div);

        count++;
    });
}

/**
 * opens the household modal for user to update the household details
 * 
 * @param {Callback} modalDetails callback
 */
function openHouseholdModal(modalDetails) {

}

function updateHousehold(incompleteKey) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len == 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
            }
        }
    }
}