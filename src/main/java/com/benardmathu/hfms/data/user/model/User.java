package com.benardmathu.hfms.data.user.model;

import com.benardmathu.hfms.data.household.model.Household;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.benardmathu.hfms.data.utils.DbEnvironment.*;

/**
 *
 * @author bernard
 */
@Getter
@Setter
@Entity
@Table(name = USERS_TB_NAME)
public class User {
    @SerializedName(USER_ID)
    @Id
    private Long id;

    @SerializedName(EMAIL)
    @Column(name = EMAIL, length = 45, unique = true)
    private String email = "";

    @SerializedName(USERNAME)
    @Column(name = USERNAME, length = 25, unique = true)
    private String username = "";
    
    @SerializedName(MOB_NUMBER)
    @Column(name = MOB_NUMBER, length = 25, unique = true, nullable = false)
    private String mobNum = "";

    @SerializedName(PASSWORD)
    @Column(name = PASSWORD, length = 255)
    private String password = "";

    @SerializedName(IS_ADMIN)
    @Column(name = IS_ADMIN)
    private boolean isAdmin = false;

    @SerializedName(IS_ONLINE)
    @Column(name = IS_ONLINE)
    private boolean isOnline = false;
    
    @SerializedName(SALT)
    @Column(name = SALT, length = 30)
    private String salt = "";

    @SerializedName("household")
    @ManyToOne(optional = false)
    @JoinColumn(name = HOUSEHOLD_ID, referencedColumnName = HOUSEHOLD_ID)
    private Household household;
}
