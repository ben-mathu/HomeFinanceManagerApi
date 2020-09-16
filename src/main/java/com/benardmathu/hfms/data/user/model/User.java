package com.benardmathu.hfms.data.user.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import com.benardmathu.hfms.init.Table;
import com.benardmathu.hfms.utils.Constants;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 *
 * @author bernard
 */
@Table(tableName = USERS_TB_NAME)
public class User {
    @SerializedName(USER_ID)
    @PrimaryKey(columnName = USER_ID)
    private String userId = "";

    @SerializedName(EMAIL)
    @Column(columnName = EMAIL, characterLength = 45, unique = true)
    private String email = "";

    @SerializedName(USERNAME)
    @Column(columnName = USERNAME, characterLength = 25, unique = true)
    private String username = "";
    
    @SerializedName(MOB_NUMBER)
    @Column(columnName = MOB_NUMBER, characterLength = 25, unique = true, notNull = false)
    private String mobNum = "";

    @SerializedName(PASSWORD)
    @Column(columnName = PASSWORD, characterLength = 255)
    private String password = "";

    @SerializedName(IS_ADMIN)
    @Column(columnName = IS_ADMIN)
    private boolean isAdmin = false;

    @SerializedName(IS_ONLINE)
    @Column(columnName = IS_ONLINE)
    private boolean isOnline = false;
    
    @SerializedName(SALT)
    @Column(columnName = SALT, characterLength = 30)
    private String salt = "";

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
    
    public void setMobNum(String mobNum) {
        this.mobNum = mobNum;
    }

    public String getMobNum() {
        return mobNum;
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

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }
}
