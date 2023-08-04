package com.benardmathu.hfms.data.income.model;

import com.google.gson.annotations.SerializedName;
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
 * @author bernard
 */
@Getter
@Setter
@Entity
@Table(name = DbEnvironment.INCOME_TB_NAME)
public class Income {
    @SerializedName(DbEnvironment.INCOME_ID)
    @Id
    private Long incomeId;

    @SerializedName(DbEnvironment.AMOUNT)
    @Column(name = DbEnvironment.AMOUNT)
    private double amount = 0;

    @SerializedName(DbEnvironment.INCOME_TYPE)
    @Column(name = DbEnvironment.INCOME_TYPE)
    private String accountType = "";

    @SerializedName(DbEnvironment.USER_ID)
    @Column(name = DbEnvironment.USER_ID)
    private String userId = "";
    
    @SerializedName(DbEnvironment.SCHEDULED_FOR)
    @Column(name = DbEnvironment.SCHEDULED_FOR, length = 25)
    private String schedule = "";

    @SerializedName(DbEnvironment.CREATED_AT)
    @Column(name = DbEnvironment.CREATED_AT, length = 25)
    private String createdAt = "";
}
