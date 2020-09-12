package com.benardmathu.hfms.data.household.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.PrimaryKey;
import com.benardmathu.hfms.init.Table;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = HOUSEHOLD_TB_NAME)
public class Household {
    @SerializedName(HOUSEHOLD_ID)
    @PrimaryKey(columnName = HOUSEHOLD_ID)
    private String id = "";

    @SerializedName(HOUSEHOLD_NAME)
    @Column(columnName = HOUSEHOLD_NAME, unique = true, characterLength = 25)
    private String name = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
