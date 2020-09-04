package com.miiguar.hfms.data.tablerelationships.jargroceryrel;

import com.google.gson.annotations.SerializedName;
import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.Table;

/**
 * Defines a relationship table.
 * @author bernard
 */
@Table(tableName = MONEY_JAR_LIST_REL_TB_NAME,
        constraint = {@Constraint(
                name = FK_JAR_GROCERY_REL_JAR_ID,
                columnName = MONEY_JAR_ID ,
                tableName = MONEY_JAR_TB_NAME
        ), @Constraint(
                name = FK_JAR_GROCERY_REL_GROCERY_ID,
                columnName = GROCERY_ID,
                tableName = GROCERIES_TB_NAME
        )}
)
public class MoneyJarGroceriesRel {
    @Column(columnName = MONEY_JAR_ID)
    private String jarId;
    
    @Column(columnName = GROCERY_ID)
    private String groceryId;

    public void setGroceryId(String groceryId) {
        this.groceryId = groceryId;
    }

    public String getGroceryId() {
        return groceryId;
    }

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public String getJarId() {
        return jarId;
    }
    
    
}
