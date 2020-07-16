package com.miiguar.hfms.data.code.model;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.PrimaryKey;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = CODE_TB_NAME,
        constraint = {@Constraint(
                name = FK_TB_CODE_USER_ID,
                tableName = USERS_TB_NAME,
                columnName = USER_ID
        )
})
public class Code {
    @SerializedName(CODE)
    @PrimaryKey(columnName = CODE)
    private String code = "";

    @SerializedName(USER_ID)
    @Column(columnName = USER_ID, unique = true)
    private String userId = "";

    @SerializedName(EMAIL_CONFIRMED)
    @Column(columnName = EMAIL_CONFIRMED)
    private boolean isEmailConfirmed = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isEmailConfirmed() {
        return isEmailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        isEmailConfirmed = emailConfirmed;
    }
}
