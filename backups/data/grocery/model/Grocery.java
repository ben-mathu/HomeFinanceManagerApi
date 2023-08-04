package com.benardmathu.hfms.data.grocery.model;

import com.benardmathu.hfms.data.jar.model.MoneyJar;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.persistence.*;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DbEnvironment.GROCERIES_TB_NAME)
public class Grocery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SerializedName(DbEnvironment.GROCERY_ID)
    @Column(name = DbEnvironment.GROCERY_ID)
    private String groceryId = "";

    @SerializedName(DbEnvironment.GROCERY_NAME)
    @Column(name = DbEnvironment.GROCERY_NAME)
    private String name = "";

    @SerializedName(DbEnvironment.GROCERY_DESCRIPTION)
    @Column(name = DbEnvironment.GROCERY_DESCRIPTION)
    private String description = "";

    @SerializedName(DbEnvironment.GROCERY_PRICE)
    @Column(name = DbEnvironment.GROCERY_PRICE)
    private double price;

    @SerializedName(DbEnvironment.REQUIRED_QUANTITY)
    @Column(name = DbEnvironment.REQUIRED_QUANTITY)
    private int required;

    @SerializedName(DbEnvironment.REMAINING_QUANTITY)
    @Column(name = DbEnvironment.REMAINING_QUANTITY)
    private int remaining;

    @SerializedName(DbEnvironment.MONEY_JAR_TB_NAME)
    @ManyToOne(optional = false)
    @JoinColumn(name = DbEnvironment.MONEY_JAR_ID, referencedColumnName = DbEnvironment.MONEY_JAR_ID)
    private MoneyJar jar;
}
