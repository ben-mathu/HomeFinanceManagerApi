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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = INCOME_TB_NAME)
public class Income {
    @SerializedName(INCOME_ID)
    @Id
    private Long incomeId;

    @SerializedName(AMOUNT)
    @Column(name = AMOUNT)
    private double amount = 0;

    @SerializedName(INCOME_TYPE)
    @Column(name = INCOME_TYPE)
    private String accountType = "";

    @SerializedName(USER_ID)
    @Column(name = USER_ID)
    private String userId = "";
    
    @SerializedName(SCHEDULED_FOR)
    @Column(name = SCHEDULED_FOR, length = 25)
    private String schedule = "";

    @SerializedName(CREATED_AT)
    @Column(name = CREATED_AT, length = 25)
    private String createdAt = "";
}
