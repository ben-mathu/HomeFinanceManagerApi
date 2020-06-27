package com.miiguar.hfms.data.tablerelationships;

import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import static com.miiguar.hfms.data.utils.DbEnvironment.HOUSEHOLD_TB_NAME;

/**
 * @author bernard
 */
@Table(tableName = USER_HOUSEHOLD_TB_NAME,
        constraint = {@Constraint(
                name = FK_USERS_HOUSEHOLD_HOUSEHOLD_ID,
                columnName = HOUSEHOLD_ID,
                tableName = HOUSEHOLD_TB_NAME
        ),@Constraint(
                name = FK_USERS_HOUSEHOLD_USER_ID,
                columnName = USER_ID,
                tableName = USERS_TB_NAME
        )})
public class UserHouseholdRel {
    @Column(columnName = USER_ID)
    private String userId = "";

    @Column(columnName = HOUSEHOLD_ID)
    private String houseId = "";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }
}
