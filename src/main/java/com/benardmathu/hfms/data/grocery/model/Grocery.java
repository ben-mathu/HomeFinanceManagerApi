package com.benardmathu.hfms.data.grocery.model;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.init.Constraint;
import com.benardmathu.hfms.init.PrimaryKey;
import lombok.*;
import org.apache.tomcat.Jar;

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
@Table(name = GROCERIES_TB_NAME)
public class Grocery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @SerializedName(GROCERY_ID)
    @Column(name = GROCERY_ID)
    private String groceryId = "";

    @SerializedName(GROCERY_NAME)
    @Column(name = GROCERY_NAME)
    private String name = "";

    @SerializedName(GROCERY_DESCRIPTION)
    @Column(name = GROCERY_DESCRIPTION)
    private String description = "";

    @SerializedName(GROCERY_PRICE)
    @Column(name = GROCERY_PRICE)
    private double price;

    @SerializedName(REQUIRED_QUANTITY)
    @Column(name = REQUIRED_QUANTITY)
    private int required;

    @SerializedName(REMAINING_QUANTITY)
    @Column(name = REMAINING_QUANTITY)
    private int remaining;

    @SerializedName(MONEY_JAR_ID)
    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Jar.class)
    @JoinColumn(name = MONEY_JAR_ID)
    private String jarId = "";
}
