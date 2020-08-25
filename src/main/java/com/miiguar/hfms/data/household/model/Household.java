package com.miiguar.hfms.data.household.model;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.PrimaryKey;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

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
