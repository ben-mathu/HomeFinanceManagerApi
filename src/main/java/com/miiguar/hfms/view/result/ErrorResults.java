package com.miiguar.hfms.view.result;

import com.google.gson.annotations.SerializedName;

/**
 * @author bernard
 */
public class ErrorResults {
    @SerializedName("username_error")
    private String usernameError = "";
    @SerializedName("email_error")
    private String emailError = "";
    @SerializedName("password_error")
    private String passwordError = "";
    @SerializedName("code_error")
    private String codeError;
    @SerializedName("household_id_error")
    private String householdIdError;

    public String getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public void setCodeError(String codeError) {
        this.codeError = codeError;
    }

    public String getCodeError() {
        return codeError;
    }

    public String getHouseholdIdError() {
        return householdIdError;
    }

    public void setHouseholdIdError(String householdIdError) {
        this.householdIdError = householdIdError;
    }
}
