package com.benardmathu.hfms.data.household.model;

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
@Table(name = HOUSEHOLD_TB_NAME)
public class Household {
    @SerializedName(HOUSEHOLD_ID)
    @Id
    private Long id;

    @SerializedName(HOUSEHOLD_NAME)
    @Column(name = HOUSEHOLD_NAME, unique = true, length = 25)
    private String name = "";
}
