package com.benardmathu.hfms.data.grocery.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Column;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import com.benardmathu.hfms.init.Table;
import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = GROCERIES_TB_NAME,
        constraint = {@Constraint(
                name = FK_GROCERIES_JAR_ID,
                columnName = MONEY_JAR_ID,
                tableName = MONEY_JAR_TB_NAME
        )}
)
public class Grocery {
    @SerializedName(GROCERY_ID)
    @PrimaryKey(columnName = GROCERY_ID)
    private String groceryId = "";

    @SerializedName(GROCERY_NAME)
    @Column(columnName = GROCERY_NAME, characterLength = 255)
    private String name = "";

    @SerializedName(GROCERY_DESCRIPTION)
    @Column(columnName = GROCERY_DESCRIPTION, characterLength = 255)
    private String description = "";

    @SerializedName(GROCERY_PRICE)
    @Column(columnName = GROCERY_PRICE)
    private double price;

    @SerializedName(REQUIRED_QUANTITY)
    @Column(columnName = REQUIRED_QUANTITY)
    private int required;

    @SerializedName(REMAINING_QUANTITY)
    @Column(columnName = REMAINING_QUANTITY)
    private int remaining;

    @SerializedName(MONEY_JAR_ID)
    @Column(columnName = MONEY_JAR_ID)
    private String jarId = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroceryId() {
        return groceryId;
    }

    public void setGroceryId(String groceryId) {
        this.groceryId = groceryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public String getJarId() {
        return jarId;
    }

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }
}
