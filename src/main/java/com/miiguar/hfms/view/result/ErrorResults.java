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
    
    public ErrorResults() {}

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
}
