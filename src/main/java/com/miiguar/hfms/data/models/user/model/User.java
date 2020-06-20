package com.miiguar.hfms.data.models.user.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author bernard
 */
public class User {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("email")
    private String email = "";
    @SerializedName("username")
    private String username = "";
    @SerializedName("password")
    private String password = "";
    @SerializedName("is_admin")
    private boolean isAdmin = false;
    @SerializedName("is_online")
    private boolean isOnline = false;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
