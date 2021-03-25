package com.benardmathu.hfms.data.grocery.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "groceries")
//@Table(tableName = GROCERIES_TB_NAME,
//        constraint = {@Constraint(
//                name = FK_GROCERIES_JAR_ID,
//                columnName = MONEY_JAR_ID,
//                tableName = MONEY_JAR_TB_NAME
//        )}
//)
public class Grocery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SerializedName(GROCERY_ID)
//    @PrimaryKey(columnName = GROCERY_ID)
    private String groceryId = "";

    @SerializedName(GROCERY_NAME)
//    @Column(columnName = GROCERY_NAME, characterLength = 255)
    private String name = "";

    @SerializedName(GROCERY_DESCRIPTION)
//    @Column(columnName = GROCERY_DESCRIPTION, characterLength = 255)
    private String description = "";

    @SerializedName(GROCERY_PRICE)
//    @Column(columnName = GROCERY_PRICE)
    private double price;

    @SerializedName(REQUIRED_QUANTITY)
//    @Column(columnName = REQUIRED_QUANTITY)
    private int required;

    @SerializedName(REMAINING_QUANTITY)
//    @Column(columnName = REMAINING_QUANTITY)
    private int remaining;

    @SerializedName(MONEY_JAR_ID)
    @Column(name = "")
//    @Column(columnName = MONEY_JAR_ID)
    private String jarId = "";
}
