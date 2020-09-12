package com.benardmathu.hfms.data.grocery;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.grocery.model.Grocery;
import com.benardmathu.hfms.data.user.model.User;
import static com.benardmathu.hfms.utils.Constants.GROCERY;
import static com.benardmathu.hfms.utils.Constants.USER;

/**
 * @author bernard
 */
public class GroceryDto {
    @SerializedName(GROCERY)
    private Grocery grocery;
    @SerializedName(USER)
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
