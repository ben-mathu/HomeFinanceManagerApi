let householdDesc;
let householdName;
let householdId;
let infoHouseholdFields;
let checkbox;

window.onload = function() {
    
    let checkboxholder = document.getElementById("checkboxItem");
    checkbox = document.getElementById("checkbox");

    householdName = document.getElementById("householdName");
//    householdDesc = document.getElementById("householdDesc");
    householdId = document.getElementById("householdId");
    infoHouseholdFields = document.getElementById("householdFields");

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
        infoHouseholdFields.innerHTML = "However, this field is required";
        householdId.hidden = false;
//        householdDesc.hidden = true;
        householdName.hidden = true;
    } else {
        infoHouseholdFields.innerHTML = "You would be able to create a house once you have registered. It is okay to leave this fields blank";
        householdId.hidden = true;
//        householdDesc.hidden = false;
        householdName.hidden = false;
    }
}