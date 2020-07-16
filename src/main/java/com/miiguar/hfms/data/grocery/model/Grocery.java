package com.miiguar.hfms.data.grocery.model;

import com.google.gson.annotations.SerializedName;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.PrimaryKey;
import com.miiguar.hfms.init.Table;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Table(tableName = GROCERIES_TB_NAME,
        constraint = {@Constraint(
                name = FK_ENVELOPE_ID_GROCERY,
                columnName = ENVELOPE_ID,
                tableName = ENVELOPE_TB_NAME
        )}
)
public class Grocery {
    @SerializedName(GROCERY_ID)
    @PrimaryKey(columnName = GROCERY_ID)
    private String groceryId = "";

    @SerializedName(GROCERY_NAME)
    @Column(columnName = GROCERY_NAME, characterLength = 255, unique = true)
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

    @SerializedName(ENVELOPE_ID)
    @Column(columnName = ENVELOPE_ID)
    private String envelopeId = "";

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

    public String getEnvelopeId() {
        return envelopeId;
    }

    public void setEnvelopeId(String envelopeId) {
        this.envelopeId = envelopeId;
    }
}
