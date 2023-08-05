let householdDesc;
let householdName;
let householdId;
let checkbox;

window.onload = function() {
    let isShowingPassword = false;
    let inputPassword = document.getElementById("password");
    let spanPassword = document.querySelector("span[for='password']");
    spanPassword.style.cursor = "pointer";
    spanPassword.addEventListener("click", function (event) {
        if (!isShowingPassword) {
            inputPassword.type = "text";
            isShowingPassword = true;
        } else {
            inputPassword.type = "password";
            isShowingPassword = false;
        }
    });
    
    inputPassword.addEventListener("input", function (event) {
        if (event.target.value === "") {
            spanPassword.hidden = true;
        } else {
            spanPassword.hidden = false;
        }
    });
    
    
    
    let checkboxholder = document.getElementById("checkboxItem");
    checkbox = document.getElementById("checkbox");

    householdName = document.getElementById("householdName");
//    householdDesc = document.getElementById("householdDesc");
    householdId = document.getElementById("householdId");

    checkbox.onclick = function () {
        if (checkbox.checked) {
            checkbox.checked = false;
            showHouseholdIdField(checkbox.checked);
        } else {
            checkbox.checked = true;
            showHouseholdIdField(checkbox.checked);
        }
    };
    
    checkboxholder.onclick = function() {
        if (checkbox.checked) {
            checkbox.checked = false;
            showHouseholdIdField(checkbox.checked);
        } else {
            checkbox.checked = true;
            showHouseholdIdField(checkbox.checked);
        }
    };
};

/**
 * show or hide the id/qr code when user checks a join household.
 * @param {boolean} joinHouseholdChecked defines a checked scenario
 */
function showHouseholdIdField(joinHouseholdChecked) {
    if (joinHouseholdChecked) {
        householdId.hidden = false;
        householdName.hidden = true;
        householdName.value = "";
    } else {
        householdId.hidden = true;
        householdId.value = "";
        householdName.hidden = false;
    }
}