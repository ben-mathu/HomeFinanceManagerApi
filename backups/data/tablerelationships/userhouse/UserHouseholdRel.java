package com.benardmathu.hfms.data.tablerelationships.userhouse;

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
import static com.benardmathu.hfms.data.utils.DbEnvironment.HOUSEHOLD_TB_NAME;

/**
 * @author bernard
 */
@Getter
@Setter
@Entity
@Table(name = DbEnvironment.USER_HOUSEHOLD_TB_NAME)
public class UserHouseholdRel {
    @Id
    private Long id;

    @SerializedName(DbEnvironment.USER_ID)
    @Column(name = DbEnvironment.USER_ID)
    private Long userId;

    @SerializedName(DbEnvironment.HOUSEHOLD_ID)
    @Column(name = DbEnvironment.HOUSEHOLD_ID)
    private Long houseId;

    @SerializedName(DbEnvironment.IS_OWNER)
    @Column(name = DbEnvironment.IS_OWNER)
    private boolean isOwner = false;
}
