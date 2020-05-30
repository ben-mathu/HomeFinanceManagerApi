package com.miiguar.hfms.data.models.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.models.user.model.User;

/**
 * @author bernard
 */
public class UserRequest {
    @SerializedName("data")
    private User user;

    public UserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
