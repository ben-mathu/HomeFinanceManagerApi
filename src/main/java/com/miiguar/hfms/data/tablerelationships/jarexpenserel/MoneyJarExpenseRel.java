package com.miiguar.hfms.data.tablerelationships.jarexpenserel;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
import com.miiguar.hfms.init.Column;
import com.miiguar.hfms.init.Constraint;
import com.miiguar.hfms.init.Table;

/**
 * Relationship table money jar-expense
 * @author bernard
 */
@Table(tableName = MONEY_JAR_EXPENSE_REL_TB,
        constraint = {@Constraint(
                name = FK_JAR_EXPENSE_REL_JAR_ID,
                columnName = MONEY_JAR_ID,
                tableName = MONEY_JAR_TB_NAME
        ),@Constraint(
                name = FK_JAR_EXPENSE_REL_EXPENSE_ID,
                columnName = EXPENSE_ID,
                tableName = EXPENSES_TB_NAME
        )}
)
public class MoneyJarExpenseRel {
    @Column(columnName = MONEY_JAR_ID)
    private String jarId;
    
    @Column(columnName = EXPENSE_ID)
    private String expenseId;

    public void setJarId(String jarId) {
        this.jarId = jarId;
    }

    public String getJarId() {
        return jarId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseId() {
        return expenseId;
    }
}
