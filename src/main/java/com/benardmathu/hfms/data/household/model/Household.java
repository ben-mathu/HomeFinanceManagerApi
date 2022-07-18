package com.benardmathu.hfms.data.household.model;

import com.benardmathu.hfms.data.budget.model.Budget;
import com.benardmathu.hfms.data.user.model.User;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 * @author bernard
 */
@Getter
@Setter
@Entity
@Table(name = HOUSEHOLD_TB_NAME)
public class Household {
    @SerializedName(HOUSEHOLD_ID)
    @Id
    private Long id;

    @SerializedName(HOUSEHOLD_NAME)
    @Column(name = HOUSEHOLD_NAME, unique = true, length = 25)
    private String name = "";

    @SerializedName(BUDGET_TB_NAME)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "household")
    private List<Budget> budgetList;

    @SerializedName(USER_HOUSEHOLD_TB_NAME)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "household")
    private List<User> userList;
}
