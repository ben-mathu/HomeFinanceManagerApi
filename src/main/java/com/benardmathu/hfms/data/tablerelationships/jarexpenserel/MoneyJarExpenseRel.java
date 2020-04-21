package com.benardmathu.hfms.data.tablerelationships.jarexpenserel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * Relationship table money jar-expense
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MONEY_JAR_EXPENSE_REL_TB)
public class MoneyJarExpenseRel {
    @Id
    private Long id;

    @Column(name = MONEY_JAR_ID)
    private Long jarId;
    
    @Column(name = EXPENSE_ID)
    private Long expenseId;
}
