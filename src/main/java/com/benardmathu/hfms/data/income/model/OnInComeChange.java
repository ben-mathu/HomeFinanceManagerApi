package com.benardmathu.hfms.data.income.model;

import static com.benardmathu.hfms.data.utils.DbEnvironment.AMOUNT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.CREATED_AT;
import static com.benardmathu.hfms.data.utils.DbEnvironment.FK_INCOME_CHANGE_INCOME_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.INCOME_ID;
import static com.benardmathu.hfms.data.utils.DbEnvironment.INCOME_TB_NAME;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_CHANGE_INCOME_STATUS;
import static com.benardmathu.hfms.data.utils.DbEnvironment.ON_UPDATE_INCOME;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author bernard
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ON_UPDATE_INCOME)
public class OnInComeChange {
    @Id
    private Long id;

    @SerializedName(AMOUNT)
    @Column(name = AMOUNT)
    private double amount;
    @SerializedName(INCOME_ID)
    @Column(name = INCOME_ID, length = 45)
    private Long incomeId;
    @SerializedName(CREATED_AT)
    @Column(name = CREATED_AT, length = 255)
    private String createdAt;
    @SerializedName(ON_CHANGE_INCOME_STATUS)
    @Column(name = ON_CHANGE_INCOME_STATUS)
    private boolean onChangeStatus = false;
}
