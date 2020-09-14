// Declare settings options
let btnAccountSettings;
let btnPrefenrenceSettings;
let btnHouseholdSettings;
let btnChangePassword;
let btnSaveAccountDetails;
let btnAddMobNum;

// Pasword fields
let fldConfirmPasswd;
let fldPasswd;

//user account fields
let fldEmail;
let fldUsername;
let fldMobNum;

// sections of settings
let accountContainer;
let preferenceContainer;
let householdContainer;

let isNewPasswordShown = false;
let isConfirmPasswordShown = false;

function configureSettings() {
    
    fldEmail = document.getElementById("inputEmail");
    fldUsername = document.getElementById("inputUsername");
    fldMobNum = document.getElementById("inputMobNumber");
    
    btnAddMobNum = document.getElementById("btnAddMobNum");
    
    if (fldMobNum.placeholder !== '') {
        btnAddMobNum.hidden = true;
    }
    
    btnAddMobNum.addEventListener("click", function (event) {
        fldMobNum.type = "text";
    });
    
    btnSaveAccountDetails = document.getElementById("btnSaveAccountDetails");
    btnSaveAccountDetails.addEventListener("click", function(event) {
        changeAccountDetails(event);
    });
    
    let spanShowPassword = document.querySelector("span[for='newPassword']");
    spanShowPassword.style.cursor = "pointer";
    spanShowPassword.addEventListener("click", function (event) {
        if (isNewPasswordShown) {
            fldPasswd.type = "password";
            isNewPasswordShown = false;
        } else {
            fldPasswd.type = "text";
            isNewPasswordShown = true;
        }
    });
    
    fldPasswd = document.getElementById("newPassword");
    fldPasswd.value = "";
    fldPasswd.addEventListener("input", function (event) {
        if (event.target.value === "") {
            spanShowPassword.hidden = true;
        } else {
            spanShowPassword.hidden = false;
        }
    });
    
    let spanShowConfirmPassword = document.querySelector("span[for='confirmPassword']");
    spanShowConfirmPassword.style.cursor = "pointer";
    spanShowConfirmPassword.addEventListener("click", function (event) {
        if (isConfirmPasswordShown) {
            fldConfirmPasswd.type = "password";
            isConfirmPasswordShown = false;
        } else {
            fldConfirmPasswd.type = "text";
            isConfirmPasswordShown = true;
        }
    });
    
    fldConfirmPasswd = document.getElementById("confirmPassword");
    fldConfirmPasswd.value = "";
    fldConfirmPasswd.autocomplete = false;
    fldConfirmPasswd.addEventListener("input", function (event) {
        if (event.target.value === "") {
            spanShowConfirmPassword.hidden = true;
        } else {
            spanShowConfirmPassword.hidden = false;
        }
    });
    
    // define button to change password
    btnChangePassword = document.getElementById("btnChangePassword");
    btnChangePassword.addEventListener("click", function(event) {
        if (!isPasswordValid()) {
            return;
        }
        changePassword(event);
    });
    
    // Initialize sections of settings to use
    accountContainer = document.getElementById("accountContainer");
    preferenceContainer = document.getElementById("preferenceContainer");
    householdContainer = document.getElementById("householdContainer");
    
    btnAccountSettings = document.getElementById("accountSettings");
//    btnPrefenrenceSettings = document.getElementById("preferencesSettings");
    btnHouseholdSettings = document.getElementById("householdSettings");

    btnAccountSettings.onclick = function () {
        btnAccountSettings.classList.add("selected-option");
//        btnPrefenrenceSettings.classList.remove("selected-option");
        btnHouseholdSettings.classList.remove("selected-option");

        accountContainer.hidden = false;
        preferenceContainer.hidden = true;
        householdContainer.hidden = true;
    };

//    btnPrefenrenceSettings.onclick = function () {
//        btnAccountSettings.classList.remove("selected-option");
//        btnPrefenrenceSettings.classList.add("selected-option");
//        btnHouseholdSettings.classList.remove("selected-option");
//        accountContainer.hidden = true;
//        preferenceContainer.hidden = false;
//        householdContainer.hidden = true;
//    };

    btnHouseholdSettings.onclick = function () {
        btnAccountSettings.classList.remove("selected-option");
//        btnPrefenrenceSettings.classList.remove("selected-option");
        btnHouseholdSettings.classList.add("selected-option");

        accountContainer.hidden = true;
        preferenceContainer.hidden = true;
        householdContainer.hidden = false;
    };

    document.getElementById("logout").onclick = function() {
        logout();
    };
    
    let btnDeleteAccount = document.getElementById("btnDeleteAccount");
    btnDeleteAccount.addEventListener("click", function(event) {
        deleteUser();
    });
}

function deleteUser() {
    
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {
                logout();
                window.location.href = ctx + "/registration";
            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            } else if (request.status === 304) {
                let spanDeleteResponse = document.querySelector("span[for='btnDeleteAccount']");
                spanDeleteResponse.style.color = "#AA002E";
                spanDeleteResponse.style.fontSize = "12px";
                spanDeleteResponse.textContent = JSON.parse(request.responseText).message;
                spanDeleteResponse.style.display = "block";
                
                window.setTimeout(function() {
                    spanDeleteResponse.textContent = "";
                });
            }
        }
    };

    let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
    data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN));

    request.open("PUT", ctx + "/dashboard/account-controller?" + data, true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send();
}

/**
 * Change user password
 */
function isPasswordValid() {
    let newPass = fldPasswd.value;
    let confirmPass = fldConfirmPasswd.value;
    
    if (newPass !== confirmPass || newPass === '') {
        let passwdSpan = document.querySelector("span[for='passwordChange']");
        passwdSpan.style.color = "#AA002E";
        passwdSpan.style.fontSize = "12px";
        passwdSpan.textContent = "The password you provided do not match";
        passwdSpan.style.display = "block";
        return false;
    }
    
    return true;
}

/**
 * Changes details of the account apart from password
 * @param {event} event
 */
function changeAccountDetails(event) {
    if (fldEmail.value !== '') {
        changeEmail();
    }
    
    if (fldUsername.value !== '') {
        changeUsername();
    }
    
    if (fldMobNum.value !== '') {
        changeMobNum();
    }
}

/**
 * Change usernames address
 */
function changeUsername() {
    let newMobNum = fldMobNum.value;
    
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {

            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };

    let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
    data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN)) + "&";
    data += userFields.PHONE_NUMBER + "=" + escape(newMobNum);

    request.open("POST", ctx + "/dashboard/account-controller/change-number", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

/**
 * Change usernames address
 */
function changeUsername() {
    let newUsername = fldUsername.value;
    
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {

            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };

    let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
    data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN)) + "&";
    data += userFields.USERNAME + "=" + escape(newUsername);

    request.open("POST", ctx + "/dashboard/account-controller/change-username", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

/**
 * Change emails address
 */
function changeEmail() {
    let newEmail = fldEmail.value;
    
    let request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            if (request.status === 200) {

            } else if (request.status === 403) {
                window.location.href = ctx + "/login";
            }
        }
    };

    let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
    data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN)) + "&";
    data += userFields.EMAIL + "=" + escape(newEmail);

    request.open("POST", ctx + "/dashboard/account-controller/change-email", true);
    request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
    request.send(data);
}

/**
 * Submit password to be changed
 * @param {event} event event attribs
 */
function changePassword(event) {
    let newPass = fldPasswd.value;
    let confirmPass = fldConfirmPasswd.value;
    
    if (newPass === confirmPass) {
        let request = getXmlHttpRequest();
        
        request.onreadystatechange = function() {
            if (request.readyState === 4) {
                if (request.status === 200) {
                    
                } else if (request.status === 403) {
                    window.location.href = ctx + "/login";
                }
            }
        };
        
        let data = userFields.USER_ID + "=" + escape(window.localStorage.getItem(userFields.USER_ID)) + "&";
        data += userFields.TOKEN + "=" + escape(window.localStorage.getItem(userFields.TOKEN)) + "&";
        data += userFields.PASSWORD + "=" + escape(newPass);
        
        request.open("POST", ctx + "/dashboard/account-controller/change-password", true);
        request.setRequestHeader(requestHeader.CONTENT_TYPE, mediaType.FORM_ENCODED);
        request.send(data);
    }
}

/**
 * logs user out
 */
function logout() {
    window.localStorage.setItem("token", "");
    window.localStorage.setItem("user_id", "");
    window.localStorage.setItem("username", "");

    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
    var contextPath = document.getElementById("contextPath").value;

    window.location.href = contextPath + "/login";
}