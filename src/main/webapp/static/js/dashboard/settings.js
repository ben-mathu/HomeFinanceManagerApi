// Declare settings options
let btnAccountSettings;
let btnPrefenrenceSettings;
let btnHouseholdSettings;

// sections of settings
let accountContainer;
let preferenceContainer;
let householdContainer;

function configureSettings() {
    // Initialize sections of settings to use
    accountContainer = document.getElementById("accountContainer");
    preferenceContainer = document.getElementById("preferenceContainer");
    householdContainer = document.getElementById("householdContainer");
    
    btnAccountSettings = document.getElementById("accountSettings");
    btnPrefenrenceSettings = document.getElementById("preferencesSettings");
    btnHouseholdSettings = document.getElementById("householdSettings");

    btnAccountSettings.onclick = function () {
        btnAccountSettings.classList.add("selected-option");
        btnPrefenrenceSettings.classList.remove("selected-option");
        btnHouseholdSettings.classList.remove("selected-option");

        accountContainer.hidden = false;
        preferenceContainer.hidden = true;
        householdContainer.hidden = true;
    }

    btnPrefenrenceSettings.onclick = function () {
        btnAccountSettings.classList.remove("selected-option");
        btnPrefenrenceSettings.classList.add("selected-option");
        btnHouseholdSettings.classList.remove("selected-option");
        accountContainer.hidden = true;
        preferenceContainer.hidden = false;
        householdContainer.hidden = true;
    }

    btnHouseholdSettings.onclick = function () {
        btnAccountSettings.classList.remove("selected-option");
        btnPrefenrenceSettings.classList.remove("selected-option");
        btnHouseholdSettings.classList.add("selected-option");

        accountContainer.hidden = true;
        preferenceContainer.hidden = true;
        householdContainer.hidden = false;
    }

    document.getElementById("logout").onclick = function() {
        logout();
    }
}

function logout() {
    window.localStorage.setItem("token", "");
    window.localStorage.setItem("user_id", "");
    window.localStorage.setItem("username", "");

    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    var contextPath = document.getElementById("contextPath").value;

    window.location.href = contextPath + "/login";
}