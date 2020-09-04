package com.miiguar.hfms.data.user;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.user.model.User;

/**
 * @author bernard
 */
public class UserRequest {
    @SerializedName("user")
    private User user;
    @SerializedName("house")
    private Household household;

    public UserRequest() {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Household getHousehold() {
        return household;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }
}
