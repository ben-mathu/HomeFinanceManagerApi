package com.miiguar.hfms.data.grocery;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.data.grocery.model.Grocery;
import com.miiguar.hfms.data.user.model.User;

/**
 * @author bernard
 */
public class GroceryDto {
    @SerializedName("grocery")
    private Grocery grocery;
    @SerializedName("user")
    private User user;

    public Grocery getGrocery() {
        return grocery;
    }

    public void setGrocery(Grocery grocery) {
        this.grocery = grocery;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
