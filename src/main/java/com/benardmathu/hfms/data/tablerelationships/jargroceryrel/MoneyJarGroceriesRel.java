package com.benardmathu.hfms.data.tablerelationships.jargroceryrel;

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
 * Defines a relationship table.
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MONEY_JAR_LIST_REL_TB_NAME)
public class MoneyJarGroceriesRel {
    @Id
    private Long id;

    @Column(name = MONEY_JAR_ID)
    private String jarId;
    
    @Column(name = GROCERY_ID)
    private String groceryId;
}
